package it.chronicle.demo.Services;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import it.chronicle.demo.Models.Article;
import it.chronicle.demo.Models.Image;
import jakarta.transaction.Transactional;
import it.chronicle.demo.Repositories.ImageRepository;
import it.chronicle.demo.Utils.StringManipulation;

import org.springframework.http.*;

@Service
public class ImageServiceImp implements ImageService {

    @Autowired
    private ImageRepository ImageRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Value("${supabase.image}")
    private String supabaseImage;

    private final RestTemplate restTemplate= new RestTemplate();

    @Override
    public void saveImageOnDB(String url, Article article) {
         url = url.replace(supabaseBucket, supabaseImage);
        ImageRepository.save(Image.builder().path(url).article(article).build());
    }

    @Async
    public CompletableFuture<String> saveImageOnCloud(MultipartFile file) throws Exception {
        if(!file.isEmpty()){
            try{
                String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                String extension = StringManipulation.getFileExtension(nameFile);

                String url = supabaseUrl + supabaseBucket+ nameFile;

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

                body.add("file", file.getBytes());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type" , "image/" + extension);
                headers.set("Authorization", "Bearer " + supabaseKey);


                HttpEntity<byte[]> requestEntity = new  HttpEntity<>(file.getBytes(), headers);

                restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

                return CompletableFuture.completedFuture(url);
            } catch(Exception e){
                e.printStackTrace();
            }
        }else{
            throw new IllegalArgumentException("Il file è vuoto");
        }

        return CompletableFuture.failedFuture(null);
    }

    @Async
    @Transactional
    public void deleteImage(String imagePath) throws IOException {

        String url = imagePath.replace(supabaseImage, supabaseBucket);

        ImageRepository.deleteByPath(imagePath);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
       headers.set("Authorization", "Bearer " + supabaseKey);


        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response  = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        System.out.println(response.getBody());
    }
    
    
}

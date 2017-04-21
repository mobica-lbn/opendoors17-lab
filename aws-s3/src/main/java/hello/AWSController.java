package hello;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

@Controller
public class AWSController {

    private static final String bucketName     = "opendoors17-dev-test";
        
    @Autowired
    AmazonS3 amazonS3;

    @GetMapping("/files/{filename:.+}")
    public String showFile(@PathVariable String filename, Model model) {
    	String response = "Error";
		try {
			// Download file
			// TODO Create object of S3Object by use amazonS3.getObject and GetObjectRequest
            response = displayTextInputStream(/*createdS3Object*/.getObjectContent());
		} catch (AmazonServiceException ase) {
			response = printAWSException(ase);
		} catch (AmazonClientException ace) {
			response = printAWSClientException(ace);
		}
		catch (IOException e) {
			response = e.getMessage();
		}
    	
		System.out.println(response);
		System.out.println();
        model.addAttribute("message", response);
        return "uploadForm";
    }
    
    @GetMapping("/delete/{filename:.+}")
    public String deleteFile(@PathVariable String filename, Model model) {
    	String response = "Error";
    	
        if (filename.substring(filename.length() - 3).compareToIgnoreCase("csv") != 0) {
           response = "You can delete only file with csv extension!";
        }
        else 
        {
			try {
				// TODO Use amazonS3.deleteObject
	            response = "File '" + filename + "' deleted from S3";
			} catch (AmazonServiceException ase) {
				response = printAWSException(ase);
			} catch (AmazonClientException ace) {
				response = printAWSClientException(ace);
			}
        }

		System.out.println(response);
		System.out.println();
        model.addAttribute("message", response);
        return "uploadForm";
    }
    
	private static String displayTextInputStream(InputStream input) throws IOException {
        return IOUtils.toString(input);
	}
	
    @RequestMapping("/")
    public String upload(Model model) {
        ArrayList<String> myArray = new ArrayList<String>();
        // TODO Create object of ObjectListing by use amazonS3.listObjects and ListObjectsRequest().withBucketName
        for (S3ObjectSummary objectSummary : /*createdObjectListing*/.getObjectSummaries()) {
            myArray.add("files/" + objectSummary.getKey());
        }        

		System.out.println(myArray);
		System.out.println();
        model.addAttribute("files", myArray);
        return "uploadForm";
    }
    
    @PostMapping("/")
    public String singleFileUpload(@RequestParam("file") MultipartFile multipart,
            RedirectAttributes redirectAttributes) {
      
      String response;
      
      if (multipart.isEmpty()) {
    	  response = "You not select a file!";
      }
      else if (multipart.getOriginalFilename().substring(multipart.getOriginalFilename().length() - 3).compareToIgnoreCase("csv") != 0) {
    	  response = "File have to have csv extension!";
      }
      else {
	      try {
	          String keyName = multipart.getOriginalFilename();
	          
	          java.io.File fileToUpload = new java.io.File(multipart.getOriginalFilename());
	          fileToUpload.createNewFile(); 
	          FileOutputStream fos = new FileOutputStream(fileToUpload); 
	          fos.write(multipart.getBytes());
	          fos.close();
	          fileToUpload.deleteOnExit();
	          
	          // Upload file
	          // TODO Use amazonS3.putObject with new PutObjectRequest
	          response = "You successfully uploaded '" + keyName + "'";
	      } 
	      catch (AmazonServiceException ase) {
	    	  response = printAWSException(ase);
	      } 
	      catch (AmazonClientException ace) {
	    	  response = printAWSClientException(ace);
	      } 
	      catch (IOException e) {
	          response = e.getMessage();
	      }
      }

		System.out.println(response);
		System.out.println();
      redirectAttributes.addFlashAttribute("message", response);
      
      return "redirect:/";
   }
    
    private String printAWSClientException(AmazonClientException ace) {
        String message = "Caught an AmazonClientException, which " +
                "means the client encountered " +
                "an internal error while trying to " +
                "communicate with S3, " +
                "such as not being able to access the network.";
        message += "\nError Message: " + ace.getMessage();
        return message;
    }

    private String printAWSException(AmazonServiceException ase) {
    	String message = "Caught an AmazonServiceException, which " +
                "means your request made it " +
                "to Amazon S3, but was rejected with an error response" +
                " for some reason.";
    	message += "\nError Message:    " + ase.getMessage();
    	message += "\nHTTP Status Code: " + ase.getStatusCode();
    	message += "\nAWS Error Code:   " + ase.getErrorCode();
    	message += "\nError Type:       " + ase.getErrorType();
    	message += "\nRequest ID:       " + ase.getRequestId();
    	return message;
    }

}

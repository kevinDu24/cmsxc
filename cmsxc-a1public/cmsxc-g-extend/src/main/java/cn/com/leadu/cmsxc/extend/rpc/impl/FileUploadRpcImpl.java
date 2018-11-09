package cn.com.leadu.cmsxc.extend.rpc.impl;

import cn.com.leadu.cmsxc.common.util.ArrayUtil;
import cn.com.leadu.cmsxc.extend.response.ResponseFailEnum;
import cn.com.leadu.cmsxc.extend.response.RestResponse;
import cn.com.leadu.cmsxc.extend.response.RestResponseGenerator;
import cn.com.leadu.cmsxc.extend.rpc.FileUploadRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author qiaomengnan
 * @ClassName: FileUploadRpcImpl
 * @Description: rpc传递文件
 * @date 2018/2/6
 */
@Component
public class FileUploadRpcImpl implements FileUploadRpc {

    @Autowired
    private RestTemplate commonRestTemplate;

    /**
     * @Title:
     * @Description:  通过远程url已经定义好的参数上传文件
     * @param requestUrl
     * @param params
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/02/06 10:25:36
     */
    public ResponseEntity<RestResponse> fileUpload(String requestUrl, Map<String,Object> params){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();
            for(String paramKey : params.keySet()){
                Object paramValue =  params.get(paramKey);
                if(paramValue instanceof MultipartFile){
                    MultipartFile paramValueTmp = (MultipartFile) paramValue;
                    formParams.add(paramKey, getFileByteArray(paramValueTmp));
                }else if(paramValue instanceof List){
                    List paramValueTmp = (List)paramValue;
                    if(ArrayUtil.isNotNullAndLengthNotZero(paramValueTmp) && paramValueTmp.get(0) instanceof MultipartFile){
                        setParamFileByteArrays(formParams,paramKey,paramValueTmp);
                    }else{
                        formParams.add(paramKey,paramValue);
                    }
                }else if(paramValue instanceof MultipartFile []){
                    setParamFileByteArrays(formParams,paramKey,(MultipartFile [])paramValue);
                }else{
                    formParams.add(paramKey,paramValue);
                }
            }
            HttpEntity<MultiValueMap<String, Object>> requestParams = new HttpEntity<>(formParams, headers);
            ResponseEntity<RestResponse> result = commonRestTemplate.exchange(requestUrl,HttpMethod.POST, requestParams, RestResponse.class);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<RestResponse>(RestResponseGenerator.genFailResponse(ResponseFailEnum.FILE_UPLOAD_ERROR), HttpStatus.OK);
        }
    }

    /**
     * @Title:
     * @Description:  通过远程url和文件名及文件值上传文件
     * @param requestUrl
     * @param fileName
     * @param multipartFile
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/02/06 10:43:40
     */
    public ResponseEntity<RestResponse> fileUpload(String requestUrl, String fileName, MultipartFile multipartFile){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();
            formParams.add(fileName, getFileByteArray(multipartFile));
            HttpEntity<MultiValueMap<String, Object>> requestParams = new HttpEntity<>(formParams, headers);
            ResponseEntity<RestResponse> result = commonRestTemplate.exchange(requestUrl,HttpMethod.POST, requestParams, RestResponse.class);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<RestResponse>(RestResponseGenerator.genFailResponse(ResponseFailEnum.FILE_UPLOAD_ERROR), HttpStatus.OK);
        }
    }


    /**
     * @Title:
     * @Description:   通过远程url和文件名及文件值集合上传文件
     * @param requestUrl
     * @param fileName
     * @param multipartFiles
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/02/06 11:00:08
     */
    public ResponseEntity<RestResponse> fileUpload(String requestUrl, String fileName, List<MultipartFile> multipartFiles){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();
            for(MultipartFile multipartFile : multipartFiles){
                formParams.add(fileName,getFileByteArray(multipartFile));
            }
            HttpEntity<MultiValueMap<String, Object>> requestParams = new HttpEntity<>(formParams, headers);
            ResponseEntity<RestResponse> result = commonRestTemplate.exchange(requestUrl,HttpMethod.POST, requestParams, RestResponse.class);
            return result;
        }catch (Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<RestResponse>(RestResponseGenerator.genFailResponse(ResponseFailEnum.FILE_UPLOAD_ERROR), HttpStatus.OK);
        }
    }

    /**
     * @Title:
     * @Description:   通过远程url和文件名及文件值集合上传文件
     * @param requestUrl
     * @param fileName
     * @param multipartFiles
     * @return
     * @throws
     * @author qiaomengnan
     * @date 2018/02/06 11:00:08
     */
    public ResponseEntity<RestResponse> fileUpload(String requestUrl, String fileName, MultipartFile ...multipartFiles){
        return fileUpload(requestUrl,fileName,Arrays.asList(multipartFiles));
    }

    private void setParamFileByteArrays(MultiValueMap<String, Object> formParams,String fileName,List<MultipartFile> multipartFiles){
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                formParams.add(fileName,getFileByteArray(multipartFile));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setParamFileByteArrays(MultiValueMap<String, Object> formParams,String fileName,MultipartFile ...multipartFiles){
         setParamFileByteArrays(formParams,fileName,Arrays.asList(multipartFiles));
    }

    private ByteArrayResource getFileByteArray(MultipartFile multipartFile){
        try {
            return new ByteArrayResource(multipartFile.getBytes()) {
                @Override
                public String getFilename() {
                    return multipartFile.getOriginalFilename();
                }
            };
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}

//Initialize formats                                                                                                                                                                                         
final String[] formats = {
			        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", 
			        "yyyy-MM-dd'T'HH:mm:ss.SSSZ", 
			        "dd-MM-yyyy HH:mm:ss", 
			        "yyyy/MM/dd HH:mm:ss", 
			        "dd/MM/yyyy HH:mm:ss", 
			        "dd-MM-yyyy", 
			        "yyyy/MM/dd",
			        "yyyy-MM-dd'T'HH:mm:ss'Z'"
			    };
       
//Initialize target format                                                                                                                                                                                    
String targetFormat = "yyyy-MM-dd HH:mm:ss";

//Initilaize the converter by Paasing formats and targetformat                                                                                                                                               
ConvertDateByPattern converter = new ConvertDateByPattern(formats, targetFormat);

//Pass the jsonString to GetConvertDateByPattern method                                                                                                                                                    
String updatedJson = converter.GetConvertDateByPattern(jsonString);

package stone.exception;

/**
 * 
 * @author langhsu
 *
 */
public enum ErrorCode {
	
	// system
	ClassNotFound,
	NotFoundPath,
	UnsupportedEncoding,
	ParseException,
	MethodInvokeException,
	InvocationTargetException,
	
	// ioe
	IOE_Exception,
	
	// http
	STATUS_200,
    STATUS_400,
    STATUS_404,
    STATUS_500,
    
    // conversion
    Conversion_ERROR,
    
    // unknown
    Unknown_ERROR,
    
    // uploadFile Error
    UploadFile_Error
}

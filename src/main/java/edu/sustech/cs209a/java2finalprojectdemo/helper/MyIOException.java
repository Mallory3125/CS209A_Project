package edu.sustech.cs209a.java2finalprojectdemo.helper;

public enum MyIOException {
    IO_EXCEPTION("IOException"),
    GET_SSL("getSSLException"),
    NOT_SERIALIZABLE("NotSerializableException"),
    NO_SUCH_FILE("NoSuchFileException"),
    SOCKET_TIMEOUT("SocketTimeoutException"),
    JSON_PROCESSING("JsonProcessingException"),
    ACCESS_DENIED("AccessDeniedException"),
    MONITOR_FAILURE("MonitorException"),
    INVALID_CLASS("InvalidClassException"),
    CHANGED_CHAR_SET("ChangedCharSetException"),
    CHARACTER_CODING("CharacterCodingException"),
    CHAR_CONVERSION("CharConversionException"),
    CLOSED_CHANNEL("ClosedChannelException"),
    EOF("EOFException"),
    FILE_ALREADY_EXISTS("FileAlreadyExistsException"),
    FILE_LOCK_INTERRUPTION("FileLockInterruptionException"),
    FILE_NOT_FOUND("FileNotFoundException"),
    FILER("FilerException"),
    FILE_SYSTEM("FileSystemException"),
    HTTP_RETRY("HttpRetryException"),
    HTTP_RESPONSE("HttpResponseException"),
    I_IO("IIOException"),
    INTERRUPTED_BY_TIMEOUT("InterruptedByTimeoutException"),
    INTERRUPTED_IO("InterruptedIOException"),
    INVALID_PROPERTIES_FORMAT("InvalidPropertiesFormatException"),
    JMX_PROVIDER("JMXProviderException"),
    JMX_SERVER("JMXServerErrorException"),
    MALFORMED_URL("MalformedURLException"),
    OBJECT_STREAM("ObjectStreamException"),
    PROTOCOL("ProtocolException"),
    REMOTE("RemoteException"),
    SASL("SaslException"),
    SOCKET("SocketException"),
    SSL("SSLException"),
    SSL_HANDSHAKE("SSLHandshakeException"),
    SSL_KEY("SSLKeyException"),
    SSL_PEER_UNVERIFIED("SSLPeerUnverifiedException"),
    SSL_PROTOCOL("SSLProtocolException"),
    SYNC_FAILED("SyncFailedException"),
    UNKNOWN_HOST("UnknownHostException"),
    UNKNOWN_SERVICE("UnknownServiceException"),
    UNSUPPORTED_DATA_TYPE("UnsupportedDataTypeException"),
    UNSUPPORTED_ENCODING("UnsupportedEncodingException"),
    USER_PRINCIPAL_NOT_FOUND("UserPrincipalNotFoundException"),
    UTF_DATA_FORMAT("UTFDataFormatException"),
    ZIP("ZipException");
    private final String errorName;

    MyIOException(String errorName) {
        this.errorName = errorName;
    }

    public String getName() {
        return errorName;
    }
}

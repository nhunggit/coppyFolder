package com.example.coppyfolder;

public class Path {
    private String pathEncrypt;
    private String pathDecrypt;

    public Path(String pathEncrypt, String pathDecrypt) {
        this.pathEncrypt = pathEncrypt;
        this.pathDecrypt = pathDecrypt;
    }

    public String getPathEncrypt() {
        return pathEncrypt;
    }

    public void setPathEncrypt(String pathEncrypt) {
        this.pathEncrypt = pathEncrypt;
    }

    public String getPathDecrypt() {
        return pathDecrypt;
    }

    public void setPathDecrypt(String pathDecrypt) {
        this.pathDecrypt = pathDecrypt;
    }
}

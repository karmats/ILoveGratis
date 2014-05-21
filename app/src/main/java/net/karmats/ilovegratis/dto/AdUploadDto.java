package net.karmats.ilovegratis.dto;

import java.io.File;

public class AdUploadDto extends AdDto {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private File img;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }

}

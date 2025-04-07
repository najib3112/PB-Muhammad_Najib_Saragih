package com.example.noteshop.model;

public class ModelBarang {

    private String key;
    private String nama;
    private String merk;
    private String harga;

    // Default constructor
    public ModelBarang() {
    }

    // Constructor with parameters
    public ModelBarang(String namaBarang, String merkBarang, String hargaBarang) {
        this.nama = namaBarang;
        this.merk = merkBarang;
        this.harga = hargaBarang;
    }

    // Getter and Setter for 'key'
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Getter and Setter for 'nama'
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    // Getter and Setter for 'merk'
    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    // Getter and Setter for 'harga'
    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}

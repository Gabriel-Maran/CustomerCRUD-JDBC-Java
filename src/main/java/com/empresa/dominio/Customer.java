package com.empresa.dominio;

public class Customer {
    private int id;
    private String nome;
    private String email;
    private String telefone;


    public Customer(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Customer(int id, String nome, String email) {
        this(nome, email);
        this.id = id;
    }

    public Customer(String nome, String email, String telefone) {
        this(nome, email);
        this.telefone = telefone;
    }

    public Customer(int id, String nome, String email, String telefone) {
        this(id, nome, email);
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}

package obc.Ejercicio10_11_12.entities;

import javax.persistence.*;

@Entity
@Table(name="laptop")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private String modelo;
    private Double peso;

    public Laptop() {
    }

    public Laptop(Long id, String marca, String modelo, Double peso) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.peso = peso;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Laptop{");
        sb.append("id=").append(id);
        sb.append(", marca='").append(marca).append('\'');
        sb.append(", modelo='").append(modelo).append('\'');
        sb.append(", peso=").append(peso);
        sb.append('}');
        return sb.toString();
    }
}

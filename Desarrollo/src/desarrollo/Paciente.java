package desarrollo;


public class Paciente {
static String nombre,apellido,CI,genero,edad,peso,estatura;     

public void setNombre(String nom){
    nombre=nom;
}
public void setApellido(String ape){
    apellido=ape;
}
public void setCI(String ci){
    CI=ci;
}
public void setGenero(String gen){
    genero=gen;
}
public void setEdad(String ed){
    edad=ed;
}
public void setPeso(String pe){
    peso=pe;
}
public void setEstatura(String est){
    estatura=est;
}

public String getNombre(){
    return nombre;
}
public String getApellido(){
    return apellido;
}
public String getCI(){
    return CI;
}
public String getGenero(){
    return genero;
}
public String getEdad(){
    return edad;
}
public String getPeso(){
    return peso;
}
public String getEstatura(){
    return estatura;
}



}

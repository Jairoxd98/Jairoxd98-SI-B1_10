package Laberinto;

public class Nodo implements Comparable<Nodo> {
	
	private Nodo padre = null;
	private Estado estado;
	private int id;
	private int costo;
	private String accion;
	private double d;
	private double h;
	private double f;
	
	public Nodo(Nodo padre, Estado estado, int id, int costo, String accion, double d, double h, double f) {

		this.padre = padre;
		this.estado = estado;
		this.id = id;
		this.costo = costo;
		this.accion = accion;
		this.d = d;
		this.h = h;
		this.f = f;
	}
	
	public Nodo getPadre() {
		return padre;
	}
	public void setPadre(Nodo padre) {
		this.padre = padre;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCosto() {
		return costo;
	}
	public void setCosto(int costo) {
		this.costo = costo;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public double getD() {
		return d;
	}
	public void setD(double d) {
		this.d = d;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	public double getF() {
		return f;
	}
	public void setF(double f) {
		this.f = f;
	}
	
	@Override 
	public int compareTo (Nodo n) { //ordenamos de menor a mayor valor (f)
		if (this.getF() < n.getF()) {
			return -1;
		} else if (this.getF() > n.getF()) {
			return 1;
		} else {
			return this.getEstado().compareTo(n.getEstado());
		}
	}

	@Override
	public String toString() {
		return "Nodo ["+ id + "]["+ costo + ", ["+ estado.getId()[0] + ", "+ estado.getId()[1] + "], " + ((padre != null)?padre.getId():"null") + ", "+ accion +", "+ d +", "+ h +", "+ f +"]";
	}
	
	/*public boolean equals(Nodo n) {
		return this.getEstado().equals(n.getEstado());
	}*/
	
	public boolean equals(Object o) {//no interpretaba bien, ya está arreglado
	    Nodo x = (Nodo) o;
	    return (this.getEstado().equals(x.getEstado()));
	}
}
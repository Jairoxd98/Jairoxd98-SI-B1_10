package Laberinto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Estado {
	
	private int [] id;
	private int valor;
	private String md5; 
	private String accion;
	
	public Estado(String accion, int x, int y, int valor) {
		this.accion = accion;
		id = new int[2];
		id[0] = x;
		id[1] = y;
		this.valor=valor;
	}

	public int[] getId() {
		return id;
	}

	public void setId(int[] id) {
		this.id = id;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	@Override
	public String toString() {
		return "Estado [id=" + Arrays.toString(id) + ", valor=" + valor + ", md5=" + md5 + ", accion=" + accion + "]";
	}
	/*
	 * Metodo compareTo
	 * 	Metodo para comparar la id del estado compuesta por (x,y). Lo utilizamos para ordenar por (fila, columna)
	 */
	public int compareTo (Estado e) {
		if (this.getId()[0] < e.getId()[0]) {
			return -1;
		} else if (this.getId()[0] > e.getId()[0]) {
			return 1;
		} else 
			return (this.getId()[1] > e.getId()[1])?1:-1;
	}
	
	/*
	 * Metodo generateMD5
	 * Se utiliza para poder referenciar un nodo de forma unica aunque varios tengan el mismo estado
	 */
	public static String generateMD5(String id) throws NoSuchAlgorithmException { 

        String IdMD5;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(id.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        IdMD5 = sb.toString();

        return IdMD5;
    }
}

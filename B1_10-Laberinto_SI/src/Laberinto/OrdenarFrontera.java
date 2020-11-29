package Laberinto;

import java.util.Comparator;

public class OrdenarFrontera implements Comparator<Nodo>{
	
	private Nodo old;
	private Nodo actual;
	private Estado estado;
	
	public int compare(Nodo old,Nodo actual) {
		if(old.getF() > actual.getF())
				return 1;
		else if(old.getF() < actual.getF())
				return -1;
		else if(old.getEstado().getId()[0] > actual.getEstado().getId()[0])
			return 1;
		else if(old.getEstado().getId()[0] < actual.getEstado().getId()[0])
			return -1;
		else if(old.getEstado().getId()[1] > actual.getEstado().getId()[1])
			return 1;
		else if(old.getEstado().getId()[1] < actual.getEstado().getId()[1])
			return -1;
		else if(old.getId() > actual.getId())
			return 1;
		else if(old.getId() < actual.getId())
			return -1;
		else
			return 0;
	}

}

package br.com.ezvida.rst.model.pagination;

public class Sort {

	private boolean sorted;
	private boolean unsorted;

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public boolean isUnsorted() {
		return unsorted;
	}

	public void setUnsorted(boolean unsorted) {
		this.unsorted = unsorted;
	}

}

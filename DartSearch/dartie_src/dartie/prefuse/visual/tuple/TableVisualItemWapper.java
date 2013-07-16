package dartie.prefuse.visual.tuple;

public class TableVisualItemWapper extends TableVisualItem{

	private TableVisualItem item;
	private String name;
	
	public TableVisualItemWapper(TableVisualItem item, String name) {
		this.item = item;
		this.name = name;
	}
	/**
	 * @return Returns the item.
	 */
	public TableVisualItem getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(TableVisualItem item) {
		this.item = item;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		return name;
	}

}

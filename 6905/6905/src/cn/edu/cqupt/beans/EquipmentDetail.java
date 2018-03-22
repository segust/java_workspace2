package cn.edu.cqupt.beans;

public class EquipmentDetail {

	private String instoreYear;
	private String productModel;
	private int packageNumber;
	private double volume;
	private double weight;
	private String manufacturer;
	private String productPrice;
	private String packageDescription;
	private String matchingInstructions;
	private String PMNM;
	private String year;
	private String month;
	private String day;
	private int listId;
	private String synopsis;
	private int balanceQuantity;
	private double income;
	private double out;
	private double balance;
	private String remark;
	
	public EquipmentDetail(String instoreYear, String productModel,
			int packageNumber, double volume, double weight,
			String manufacturer, String productPrice,
			String packageDescription, String matchingInstructions,
			String pMNM, String year, String month, String day, int listId,
			String synopsis, int balanceQuantity, double income, double out,
			double balance, String remark) {
		super();
		this.instoreYear = instoreYear;
		this.productModel = productModel;
		this.packageNumber = packageNumber;
		this.volume = volume;
		this.weight = weight;
		this.manufacturer = manufacturer;
		this.productPrice = productPrice;
		this.packageDescription = packageDescription;
		this.matchingInstructions = matchingInstructions;
		PMNM = pMNM;
		this.year = year;
		this.month = month;
		this.day = day;
		this.listId = listId;
		this.synopsis = synopsis;
		this.balanceQuantity = balanceQuantity;
		this.income = income;
		this.out = out;
		this.balance = balance;
		this.remark = remark;
	}

	public EquipmentDetail() {
		super();
	}

	public String getInstoreYear() {
		return instoreYear;
	}

	public void setInstoreYear(String instoreYear) {
		this.instoreYear = instoreYear;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public int getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(int packageNumber) {
		this.packageNumber = packageNumber;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getPackageDescription() {
		return packageDescription;
	}

	public void setPackageDescription(String packageDescription) {
		this.packageDescription = packageDescription;
	}

	public String getMatchingInstructions() {
		return matchingInstructions;
	}

	public void setMatchingInstructions(String matchingInstructions) {
		this.matchingInstructions = matchingInstructions;
	}

	public String getPMNM() {
		return PMNM;
	}

	public void setPMNM(String pMNM) {
		PMNM = pMNM;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getListId() {
		return listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public int getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(int balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public double getOut() {
		return out;
	}

	public void setOut(double out) {
		this.out = out;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

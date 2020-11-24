import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class SerializationPrac {

		public static void main(String[] args) throws IOException, ClassNotFoundException {	
			Car car = new Car(200000, 2019);
			FileOutputStream fos = new FileOutputStream("C:\\BPER\\Fileoutputstream.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(car);
			
			FileInputStream fis = new FileInputStream("C:\\BPER\\Fileoutputstream.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Car c1=(Car)ois.readObject();
			System.out.println(c1.getPrice());
		}

	}

	 
	 
	 
	 
	class Car implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5629119752098370256L;
		public int getPrice() {
			return price;
		}
		public void setPrice(int price) {
			this.price = price;
		}
		public int getModel() {
			return model;
		}
		public void setModel(int model) {
			this.model = model;
		}
		int price;
		int model;
		Car(int price, int model){
			this.price = price;
			this.model = model;
		}
	}
	
	

// serializing multiple objects
	class Animal implements Serializable{
		int i=0;
	}
	class Building implements Serializable{
		int j=12;
	}
	
	class Company implements Serializable{
		int k=90;
	}
	class MultipleSerialization {
		
		public static void main(String[] args) throws Exception{
		Animal animal = new Animal();	
		Building building = new Building();
		Company company = new Company();
		
		FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\MultipleSerialization.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(animal);
		oos.writeObject(building);
		oos.writeObject(company);
		
		FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\MultipleSerialization.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Animal a1=(Animal)ois.readObject(); // order while deserializing is important, same order of deserialization should be done 
		System.out.println(a1.i);
		// if we don't remember the serialization order, then
		Object obj = ois.readObject();
		if(obj instanceof Building) {
			System.out.println(((Building) obj).j);
		}
		}
		
	}
	
	
	// object graph - while serializing any object, serializing the objects which are referring to the current object
	
	class Power implements Serializable{
		int hp = 3200;
		Electricity e = new Electricity();
	}
	class Electricity implements Serializable{
		int kw = 4300;
		Bill b = new Bill();
	}
	class Bill implements Serializable{
		int w = 20;
	}
	
	class ObjectGraph {
		public static void main(String[] args) throws Exception{
			FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\ObjectGraph.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			Power p = new Power();
			oos.writeObject(p);
			
			FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\ObjectGraph.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Power p1 = (Power)ois.readObject();
			System.out.println(p1.e.b.w);
		}
	}
	
	
	// transient with static and final keyword
	
	class Account implements Serializable{
		int id = 123;
		transient static String password = "sand";
		transient final String pin = "5322";
	}
	
	class Transient {
		public static void main(String[] ar) throws Exception{
			Account ac = new Account();
			FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\TransientStatic.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ac);
			
			FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\TransientStatic.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Account ac1 = (Account)ois.readObject();
			System.out.println(ac1.id +" "+ ac1.password+" "+ac1.pin);
			
		}
	}
	
	//customized serialization
	
	class Acc implements Serializable {
		int id=3453;
		transient String password="sand";
		private void writeObject(ObjectOutputStream oos) throws Exception{
			oos.defaultWriteObject();
			this.password = this.password.concat("123");
			oos.writeObject(this.password);
		}
		private void readObject(ObjectInputStream ois) throws Exception{
			ois.defaultReadObject();
			String pass = (String)ois.readObject();
			this.password = pass.substring(0,4);
		}
	}
	
	class CustSerialization {
		public static void main(String[] a) throws Exception{
			Acc acc = new Acc();
			FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\CustomizedSerialization.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(acc);
			
			FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\CustomizedSerialization.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Acc a2 = (Acc)ois.readObject();
			System.out.println(a2.id +" "+a2.password);
		}
	}
	
	
	//customized serialization - when there is a loss of information like sensitive information due to the transient keyword, then that info cannot be deserialized at other end. So we should go for Customized serialization which includes encoding the data at serialization and decoding the same at the time of deserialization - bus
	
	class Account1 implements Serializable{
		String username="sand";
		transient String password="sand@8790";
		
		private void writeObject(ObjectOutputStream oos) throws Exception{
			oos.defaultWriteObject();
			String modifiedPassword = this.password.concat("Varun");
			oos.writeObject(modifiedPassword);
		}
		
		private void readObject(ObjectInputStream ois) throws Exception{
			ois.defaultReadObject();
			String receivedString = (String)ois.readObject();
			this.password = receivedString.substring(0, 9);
		}
		
		
	}
	
	class CustomizedSerialization{
		public static void main(String[] a) throws Exception{
		Account1 acc = new Account1();
		FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\CustomizedSerialization2.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(acc);
		
		FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\CustomizedSerialization2.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		Account1 acc2 = (Account1)ois.readObject();
		System.out.println(acc2.username+"---"+acc2.password);
		}
	}
	
	
	
	//Externalization - If we want to save part of an object unlike in serialization, then we should go for Externalization by extending interface Externalizable. JVM will not have any control in saving the part of object to file. Everything needs to be taken care by Programmer. Hence this is not so popular. Performance wise Externalization is good becoz we can save only part of object.
	
	class Worker implements Externalizable{
		int i;
		int j;
		String point;
		
		public Worker() {
			System.out.println("no arg constructor is executed");
		}
		
		public Worker(int i, int j, String point) {
			this.i = i;
			this.j = j;
			this.point = point;
		}
		@Override
		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeInt(i);
			out.writeObject(point);
			
		}
		@Override
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			this.i = in.readInt();
			this.point=(String)in.readObject();
			
		}
	}
	
	class ExternalizableExample{
		
		public static void main(String[] a) throws Exception{
			Worker work = new Worker(10, 20, "blank");
			FileOutputStream fos = new FileOutputStream("C:\\BPER\\serialization prac\\Externalization.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(work);  //while writing to file, jvm will check if the object is implementing Serializable or Externalizable, if it is serializable then it will save whole object or call private writeObject() method. If it is externalizable then jvm doesn't know which fields to be serialized, so it will check writeExternal method in class.
			
			System.out.println("Deserialization started");
			FileInputStream fis = new FileInputStream("C:\\BPER\\serialization prac\\Externalization.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Worker work2 = (Worker)ois.readObject(); //while reading from file, jvm will check if object is implementing Externalizable, if so, then jvm will create a new object by running no argument constructor and create new object and then on this object it will call readExternal() method
			System.out.println(work2.i+" "+work2.j+" "+work2.point);
		}
	}

	
	// use of serialVersionUID:
	// while Serialization, jvm will save an unique code with the object. This code will be useful at the deserialization end, jvm of the other machine where deserialization is performed, there jvm will compare the code with the UID code of the local class, if both matches, then only Deserialization happens
	// otherwise InvalidClassException will arise. 
	// Now if we depend on the jvm created UID code, then after serialization, for suppose if the class variables are changed then UID code will be changed. Hence deserialization does not happen.
	// Hence we should define our own public static final Generated SerialVersionUID for the class. So that it will not be changed even if we change the variables.

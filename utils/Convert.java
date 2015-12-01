package utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Convert {

	//Converts any object to byte array
	public static byte[] toByteArray(Object object){
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo;
		byte[] b = null;
		try {
			bo.flush();
			oo = new ObjectOutputStream(bo);
			oo.flush();
			oo.writeObject(object);
			b = bo.toByteArray();
			oo.flush();
			oo.close();
			bo.flush();
			bo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return b;
	}
	
	//Converts byte array to any object typecasted
	public static Object ByteArrayToObject(byte[] array){
		ByteArrayInputStream bi = new ByteArrayInputStream(array);
		ObjectInputStream oi;
		Object o = null;
		try {
			oi = new ObjectInputStream(bi);
			o = oi.readObject();
			oi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
		
}

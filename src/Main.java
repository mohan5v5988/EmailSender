import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Main {
	// static ObjectMapper mapper = new ObjectMapper();
	// mapper.writeValueAsString(data)
	// mapper.readValue(json, Data.class)
	public static void main(String[] args) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(args[0]));
			if(props.containsKey("tsl")) {
				new TLS().send(props);
			} else {
				new SSL().send(props);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
package trial;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadFile {

	public static void main(String[] args) {

		BufferedReader reader = null;
		try {
			String filePathStr = "C:/data/home/eiichi/Workspaces/space2/TrialPentahoDI/src/test/ktr/data_text.csv";

			Path filePath = Paths.get(filePathStr, "");
			reader = Files.newBufferedReader(filePath);

			String line = null;
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			reader.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
	}

}

package com.gls.ppldv.user.util;

import java.io.IOException;
import java.util.UUID;

public class FileUtil {
	
	
	
	public String uploadFile(String original, byte[] fileData) throws IOException {
		String savedFileName = "";
		String uuid = UUID.randomUUID().toString(); // �� 32���� ���� ���� + 4���� -���� ���յ� 36�ڸ� ����
		savedFileName = uuid.replace("-", "")+"_"+original; // ����Ǵ� ���� �̸�
		
		//Spring ���� �����ϴ� ���� ���� ��ü�� �̿��� ������ ��ġ�� ���� ���ε�
		
		
		return null;
	}
}

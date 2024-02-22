package com.gls.ppldv.user.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect // AOPAdvice Ŭ�������� ���
@Slf4j
@Component // component ���
@NoArgsConstructor
public class AOPAdvice {
	
	// target joinPoint(method)�� ���� �Ǳ� �� ȣ�� (execution({��ȯŸ��} {������ �޼ҵ� ��ġ}))
		// @Before("execution(* com.bitc.service.*.*(..))") - ��� ��ȯŸ��, Ʈ���� ��� Ŭ�����߿��� ��� �޼ҵ� ��� �Ű�����(Ÿ��, ����) (com.gyumin.service(Ʈ��) .*(���Ŭ����).*(���޼���) (..)(���Ű�����)
		@Before("execution(* com.gls.ppldv.user.service().*.*(..))")
		public void startLog(JoinPoint jp) {
			log.info("--------------------------------------");
			log.info("--------------------------------------");
			log.info("------------- START LOG --------------");
			log.info("target : {}", jp.getTarget()); // �츮�� �������� ������ Ŭ����(execution)
			log.info("type : {}", jp.getKind()); // Ÿ���� ���� �˷��� (ȣ��� ����ڰ� method�� method-execution)
			log.info("parameters : {}", Arrays.toString(jp.getArgs())); // �޼ҵ��� �Ű������� �������� �� �����Ƿ�
			log.info("name : {}", jp.getSignature().getName()); // Ÿ���� �Ǵ� joinpoint �޼ҵ� �̸�
			log.info("----------- START LOG END ------------");
		}
		
		@After("execution(* com.gls.ppldv.service.().*(..))")
		public void endLog() {
			log.info("------------END AFTER LOG-------------");
			log.info("--------------------------------------");
		}
}

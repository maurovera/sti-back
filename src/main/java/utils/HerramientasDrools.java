package utils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class HerramientasDrools {

	private String reglasDRL;
	private KnowledgeBase baseConocimiento;
	private StatefulKnowledgeSession ksession;

	public HerramientasDrools(String reglasDRL) {
		this.reglasDRL = reglasDRL;
		this.baseConocimiento = null;
		this.ksession = null;

	}

	public void iniciarBaseConocimiento() {
		try {
			baseConocimiento = readKnowledgeBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ejecutarReglas(List<Regla> reglas) {
		for (Regla r : reglas) {
			ksession.insert(r);
		}
		ksession.fireAllRules();

	}

	public void ejecutarRegla(Regla regla) {
		ksession.insert(regla);
		ksession.fireAllRules();
	}

	public void terminarSession() {
		ksession.dispose();
	}

	public void iniciarSession() {
		ksession = baseConocimiento.newStatefulKnowledgeSession();
	}

	private KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();

		Resource resource = ResourceFactory.newByteArrayResource(reglasDRL
				.getBytes(StandardCharsets.UTF_8));
		kbuilder.add(resource, ResourceType.DRL);

		
		KnowledgeBuilderErrors errors = kbuilder.getErrors();

		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("No se pudo generar la base de conocimientos.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}
}

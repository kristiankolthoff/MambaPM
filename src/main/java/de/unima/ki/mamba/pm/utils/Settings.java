package de.unima.ki.mamba.pm.utils;

import com.google.inject.AbstractModule;

import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.ConfigurationModule;
import net.jmob.guice.conf.core.InjectConfig;
import net.jmob.guice.conf.core.Syntax;

import static com.google.inject.Guice.createInjector;

@BindConfig(value = "mambapm", syntax = Syntax.PROPERTIES)
public class Settings {

	@InjectConfig(value = "WORDNET")
	private static String wordnetDirectory;
	@InjectConfig(value = "WORDSPACE_ENWIKI")
	private static String wordspaceEnwikiDirectory;
	@InjectConfig(value = "WORDSPACE_WORD2VEC")
	private static String wordspaceWord2VecDirectory;
	@InjectConfig(value = "POS_TAGGER")
	private static String posTaggerDirectory;
	@InjectConfig(value = "JAVA_HOME")
	private static String javaHomeDirectory;
	
	static {
		createInjector(new GuiceModule(new Settings()));
	}
	
	private Settings() {
	}
	
	public static String getWordnetDirectory() {
		return wordnetDirectory;
	}

	public static String getWordspaceEnwikiDirectory() {
		return wordspaceEnwikiDirectory;
	}

	public static String getWordspaceWord2VecDirectory() {
		return wordspaceWord2VecDirectory;
	}

	public static String getPosTaggerDirectory() {
		return posTaggerDirectory;
	}

	public static String getJavaHomeDirectory() {
		return javaHomeDirectory;
	}

	private static class GuiceModule extends AbstractModule {

        private final Object obj;

        public GuiceModule(Object obj) {
            this.obj = obj;
        }

        @Override
        protected void configure() {
            install(ConfigurationModule.create());
            requestInjection(obj);
        }
    }


	public static void main(String[] args) {
		System.out.println(Settings.getWordnetDirectory());
		System.out.println(Settings.getWordspaceEnwikiDirectory());
		System.out.println(Settings.getWordspaceWord2VecDirectory());
		System.out.println(Settings.getPosTaggerDirectory());
		System.out.println(Settings.getJavaHomeDirectory());
	}
}


package org.codehaus.groovy.grails.compiler.web.pages;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.grails.compiler.injection.ClassInjector;
import org.codehaus.groovy.grails.compiler.injection.GrailsAwareInjectionOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * A GroovyPage compiler injection operation that uses a specified array of ClassInjector instances to
 * attempt AST injection.
 *
 * @author Stephane Maldini
 * @since 1.4
 */
public class GroovyPageInjectionOperation extends GrailsAwareInjectionOperation {

    public GroovyPageInjectionOperation() {
        super();
    }

    private GroovyPageInjector[] groovyPageInjectors;

    public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) throws CompilationFailedException {
        for (GroovyPageInjector classInjector : getGroovyPageInjectors()) {
            try {
                classInjector.performInjection(source, context, classNode);
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println("Error occurred calling AST injector [" + classInjector.getClass() + "]: " + e.getMessage());
                throw e;
            }
        }
    }

    private GroovyPageInjector[] getGroovyPageInjectors() {
         if(groovyPageInjectors == null) {
             List<GroovyPageInjector> injectors = new ArrayList<GroovyPageInjector>();
             for(ClassInjector ci : getClassInjectors()){
                 if(ci instanceof GroovyPageInjector){
                     injectors.add((GroovyPageInjector)ci);
                 }
             }
             groovyPageInjectors = injectors.toArray(new GroovyPageInjector[injectors.size()]);
        }
        return groovyPageInjectors;
    }
}

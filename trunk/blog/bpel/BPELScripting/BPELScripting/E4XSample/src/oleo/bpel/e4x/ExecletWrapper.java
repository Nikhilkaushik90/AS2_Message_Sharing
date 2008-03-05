package oleo.bpel.e4x;

import com.collaxa.cube.CubeException;
import com.collaxa.cube.engine.ICubeContext;
import com.collaxa.cube.engine.core.IScope;
import com.collaxa.cube.engine.core.IWorkItem;
import com.collaxa.cube.engine.ext.BPELXExecLetUtil;
import com.collaxa.cube.xml.dom.DOMUtil;

import com.oracle.bpel.client.BPELFault;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.xml.XMLObject;
import org.mozilla.javascript.xmlimpl.XMLLibImpl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Scriptable wrapper for the <code>ExecletContext</code>. This
 * is needed for a transparent transformation between org.w3c.dom.Node 
 * and Rhino's XML implementation and vise versa.
 * 
 * The code is based on org.apache.ode.extension.e4x.ExtensionContextWrapper  from Apache ODE  project, 
 * Original class is created by  Tammo van Lessen (University of Stuttgart)
 */
public class ExecletWrapper extends ScriptableObject {
    private static final long serialVersionUID = 1L;

    private BPELXExecLetUtil execlet;
    private Context sctx;

    public ExecletWrapper() {
        super();
    }

    public ExecletWrapper(IWorkItem wi, IScope sc, ICubeContext ctx, 
                          Context sctx) {
        super();
        execlet = new BPELXExecLetUtil();
        execlet.init(wi, sc, ctx);
        this.sctx = sctx;
    }

    public String getClassName() {
        return "ExecletContext";
    }

    public static Scriptable jsConstructor(Context cx, Object[] args, 
                                           Function ctorObj, 
                                           boolean inNewExpr) {
        if (args.length == 4 && (args[0] instanceof IWorkItem) && 
            (args[1] instanceof IScope) && (args[2] instanceof ICubeContext) && 
            (args[3] instanceof Context)) {
            return new ExecletWrapper((IWorkItem)args[0], (IScope)args[1], 
                                      (ICubeContext)args[2], (Context)args[3]);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void jsFunction_addAuditTrail(String string) {
        execlet.addAuditTrailEntry(string);
    }

    public void jsFunction_writeVariable(String varName, XMLObject node) {
        Node n = js2node(node);
        setVariable(varName, n);
    }

    public void jsFunction_writePart(String varName, String part, 
                                     XMLObject node) {
        Node n = js2node(node);
        setPart(varName, part, n);
    }

    public XMLObject jsFunction_readVariable(String varName) throws TransformerException {

        Node n = (Node)getVariable(varName);

        return node2js(n);
    }

    public XMLObject jsFunction_readPart(String varName, 
                                         String part) throws TransformerException {

        Node n = (Node)getPart(varName, part);

        return node2js(n);
    }

    private Node js2node(XMLObject xml) {
        Node n = XMLLibImpl.toDomNode(xml);
        return n;
    }

    public XMLObject node2js(Node n) throws TransformerException {
        Scriptable parentScope = getParentScope();

        return (XMLObject)sctx.newObject(parentScope, "XML", 
                                          new Object[] { Context.javaToJS(domToString(n), 
                                                                          parentScope) });
    }

    public static String domToString(Node n) throws TransformerException {
        TransformerFactory xformFactory = TransformerFactory.newInstance();
        Transformer idTransform = xformFactory.newTransformer();
        idTransform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        Source input = new DOMSource(n);
        StringWriter sw = new StringWriter();
        Result output = new StreamResult(sw);
        idTransform.transform(input, output);

        return sw.toString();
    }

    private Object getVariable(String name) {
        Object result = null;
        try {
            return result = execlet.getVariableData(name);
        } catch (BPELFault e) {
            throw new NullPointerException("no var for " + name);
        }

    }

    private Object getPart(String name, String part) {
        Object result = null;
        try {
            result = execlet.getVariableData(name, part);

            return result;

        } catch (BPELFault e) {
            throw new NullPointerException("no var for " + name + " part " + 
                                           part);
        }
    }

    private void setVariable(String name, Object value) {
        try {
            execlet.setVariableData(name, value);
        } catch (BPELFault e) {
            e.printStackTrace();
        }
    }

    private void setPart(String name, String partOrQuery, Object value) {
        try {
            // Some magic with DOMUtil.adjustPartValue(). I just guessed.
            execlet.setVariableData(name, partOrQuery, 
                                    DOMUtil.adjustPartValue((Element)value));
        } catch (BPELFault e) {
            e.printStackTrace();
        } catch (CubeException e) {
            e.printStackTrace();
        }
    }
}

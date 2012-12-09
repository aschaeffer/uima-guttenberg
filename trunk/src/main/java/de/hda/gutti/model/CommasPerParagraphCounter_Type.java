
/* First created by JCasGen Thu Dec 06 12:19:47 CET 2012 */
package de.hda.gutti.model;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** Commas Per Paragraph Counter
 * Updated by JCasGen Thu Dec 06 12:19:47 CET 2012
 * @generated */
public class CommasPerParagraphCounter_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CommasPerParagraphCounter_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CommasPerParagraphCounter_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CommasPerParagraphCounter(addr, CommasPerParagraphCounter_Type.this);
  			   CommasPerParagraphCounter_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CommasPerParagraphCounter(addr, CommasPerParagraphCounter_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CommasPerParagraphCounter.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.hda.gutti.model.CommasPerParagraphCounter");
 
  /** @generated */
  final Feature casFeat_count;
  /** @generated */
  final int     casFeatCode_count;
  /** @generated */ 
  public int getCount(int addr) {
        if (featOkTst && casFeat_count == null)
      jcas.throwFeatMissing("count", "de.hda.gutti.model.CommasPerParagraphCounter");
    return ll_cas.ll_getIntValue(addr, casFeatCode_count);
  }
  /** @generated */    
  public void setCount(int addr, int v) {
        if (featOkTst && casFeat_count == null)
      jcas.throwFeatMissing("count", "de.hda.gutti.model.CommasPerParagraphCounter");
    ll_cas.ll_setIntValue(addr, casFeatCode_count, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CommasPerParagraphCounter_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_count = jcas.getRequiredFeatureDE(casType, "count", "uima.cas.Integer", featOkTst);
    casFeatCode_count  = (null == casFeat_count) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_count).getCode();

  }
}



    
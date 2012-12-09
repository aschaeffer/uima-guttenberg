

/* First created by JCasGen Thu Dec 06 12:19:47 CET 2012 */
package de.hda.gutti.model;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** Commas Per Paragraph Counter
 * Updated by JCasGen Thu Dec 06 12:19:47 CET 2012
 * XML source: /home/aschaeffer/workspace4/uima-guttenberg/src/test/resources/desc/type/CommasPerParagraphCounter.xml
 * @generated */
public class CommasPerParagraphCounter extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CommasPerParagraphCounter.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CommasPerParagraphCounter() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CommasPerParagraphCounter(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CommasPerParagraphCounter(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public CommasPerParagraphCounter(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: count

  /** getter for count - gets Commas Per Paragraph
   * @generated */
  public int getCount() {
    if (CommasPerParagraphCounter_Type.featOkTst && ((CommasPerParagraphCounter_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "de.hda.gutti.model.CommasPerParagraphCounter");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CommasPerParagraphCounter_Type)jcasType).casFeatCode_count);}
    
  /** setter for count - sets Commas Per Paragraph 
   * @generated */
  public void setCount(int v) {
    if (CommasPerParagraphCounter_Type.featOkTst && ((CommasPerParagraphCounter_Type)jcasType).casFeat_count == null)
      jcasType.jcas.throwFeatMissing("count", "de.hda.gutti.model.CommasPerParagraphCounter");
    jcasType.ll_cas.ll_setIntValue(addr, ((CommasPerParagraphCounter_Type)jcasType).casFeatCode_count, v);}    
  }

    
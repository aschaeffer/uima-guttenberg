

/* First created by JCasGen Tue Dec 11 22:33:30 CET 2012 */
package de.hda.gutti.model;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** An unknown word
 * Updated by JCasGen Tue Dec 11 22:33:30 CET 2012
 * XML source: /home/aschaeffer/workspace4/uima-guttenberg/src/test/resources/desc/type/UnknownWord.xml
 * @generated */
public class UnknownWord extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UnknownWord.class);
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
  protected UnknownWord() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public UnknownWord(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public UnknownWord(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public UnknownWord(JCas jcas, int begin, int end) {
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
     
}

    
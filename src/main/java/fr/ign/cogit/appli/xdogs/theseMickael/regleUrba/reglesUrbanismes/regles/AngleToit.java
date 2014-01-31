//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.1.3-b01-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2009.10.26 at 12:24:15 PM CET
//

package fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.reglesUrbanismes.regles;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.Executor;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.representation.Incoherence;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Batiment;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Parcelle;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.schemageo.Toit;
import fr.ign.cogit.appli.xdogs.theseMickael.regleUrba.util.Prospect;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IEnvelope;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.contrib.geometrie.Angle;
import fr.ign.cogit.geoxygene.contrib.geometrie.Vecteur;
import fr.ign.cogit.geoxygene.sig3d.convert.java3d.ConversionJava3DGeOxygene;
import fr.ign.cogit.geoxygene.sig3d.equation.ApproximatedPlanEquation;
import fr.ign.cogit.geoxygene.sig3d.geometry.Box3D;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPosition;
import fr.ign.cogit.geoxygene.spatial.coordgeom.DirectPositionList;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;
import fr.ign.cogit.geoxygene.spatial.geomprim.GM_OrientableSurface;

/**
 * <p>
 * Java class for AngleToit complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AngleToit">
 *   &lt;complexContent>
 *     &lt;extension base="{}Consequence">
 *       &lt;sequence>
 *         &lt;element name="AngleMin" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="AngleMax" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AngleToit", propOrder = { "angleMin", "angleMax" })
public class AngleToit extends Consequence {

  public AngleToit() {
    super();
  }

  public AngleToit(double angleMin, double angleMax) {
    super();
    this.angleMin = angleMin;
    this.angleMax = angleMax;
  }

  @XmlElement(name = "AngleMin")
  protected double angleMin;
  @XmlElement(name = "AngleMax")
  protected double angleMax;

  /**
   * Gets the value of the angleMin property.
   */
  public double getAngleMin() {
    return this.angleMin;
  }

  /**
   * Sets the value of the angleMin property.
   */
  public void setAngleMin(double value) {
    this.angleMin = value;
  }

  /**
   * Gets the value of the angleMax property.
   */
  public double getAngleMax() {
    return this.angleMax;
  }

  /**
   * Sets the value of the angleMax property.
   */
  public void setAngleMax(double value) {
    this.angleMax = value;
  }

  @Override
  public String toString() {

    return "L'angle du batiment doit être compris entre " + this.angleMin
        + "° et " + this.angleMax + "°";
  }

  // On regarde l'angle des toits par rapport à l'horizontal
  @SuppressWarnings("unchecked")
  @Override
  public List<Incoherence> isConsequenceChecked(Parcelle p, boolean represent) {
    List<Incoherence> lIncoherence = new ArrayList<Incoherence>();
    // On parcourt la liste des batiments
    List<Batiment> lBatiments = p.getlBatimentsContenus();

    int nbBatiments = lBatiments.size();

    for (int i = 0; i < nbBatiments; i++) {
      Toit t = lBatiments.get(i).getToit();

      if (t == null) {
        continue;
      }

      IGeometry geom = t.getGeom();

      try {
        GM_MultiSurface<GM_OrientableSurface> geomToit = (GM_MultiSurface<GM_OrientableSurface>) geom;

        int nbPans = geomToit.size();

        for (int j = 0; j < nbPans; j++) {
          GM_OrientableSurface surf = geomToit.get(j);

          ApproximatedPlanEquation eqP = new ApproximatedPlanEquation(surf);

          Vecteur normal = eqP.getNormale();

          // On s'assure que la normale est orientée vers le haut
          // Ca nous assure d'avoir un angle de toit
          // entre 0 et PI
          if (normal.getZ() < 0) {
            normal.setX(-normal.getX());
            normal.setY(-normal.getY());
            normal.setZ(-normal.getZ());
          }

          // On calcul l'angle entre la normale et l'horizontale
          Vecteur horizontal = new Vecteur(normal.getX(), normal.getY(), 0);

          Angle angle = horizontal.angleVecteur(normal);

          // Il y a 90° de différence entre la normale et le plan du
          // toit
          double angleFinal = angle.getValeur();

          double angleFinaleDegree = Math.toDegrees(angleFinal);

          System.out.println(angleFinaleDegree);

          if (angleFinaleDegree < this.angleMin) {

            if (Executor.VERBOSE) {
              System.out
                  .println("Conséquence non vérifiée : angle min des toits");

            }
            /*
             * lIncoherence.add(new Incoherence(this, p, this
             * .genereBranchGroup(t, this.angleMin))); break;
             */
            
            if(represent){

            lIncoherence.add(new Incoherence(this, p, this
                .genereBranchGroupProposition2(surf, false)));
            
            }else{
              lIncoherence.add(null);
              return lIncoherence;
            }
            continue;
          } else if (angleFinaleDegree > this.angleMax) {

            if (Executor.VERBOSE) {
              System.out
                  .println("Conséquence non vérifiée : angle max des toits");

            }
            /*
             * lIncoherence.add(new Incoherence(this, p, this
             * .genereBranchGroup(t, this.angleMax))); break;
             */

            
            
            if(represent){
            lIncoherence.add(new Incoherence(this, p, this
                .genereBranchGroupProposition2(surf, true)));
            continue;
            }else{
              lIncoherence.add(null);
              return lIncoherence;
            }
          }

        }

      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Conversion échouée");
      }

    }

    if (Executor.VERBOSE) {
      if (lIncoherence.size() == 0) {
        System.out.println("Conséquence vérifiée : angle des toits");
      }
    }

    return lIncoherence;
  }

  public BranchGroup genereBranchGroupProposition2(IOrientableSurface surf,
      boolean max) {
    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(4);
    lOS.add(surf);

    Shape3D shape = null;
    if (max) {
      shape = new Shape3D(ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lOS).getGeometryArray(),
          Prospect.genereApparence(true, Color.red, 1, true));

    } else {

      shape = new Shape3D(ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lOS).getGeometryArray(),
          Prospect.genereApparence(true, Color.green, 1, true));

    }

    BranchGroup bg = new BranchGroup();

    bg.addChild(shape);

    return bg;

  }

  public BranchGroup genereBranchGroup(Toit t, double angle) {
    BranchGroup bg = new BranchGroup();

    IEnvelope env = t.getGeom().envelope();

    Box3D b = new Box3D(t.getGeom());

    double zMax = b.getURDP().getZ();

    IDirectPosition dp1 = env.getUpperCorner();
    dp1.setZ(zMax);
    IDirectPosition dp3 = env.getLowerCorner();
    dp3.setZ(dp1.getZ());

    DirectPosition dp2 = new DirectPosition(dp3.getX(), dp1.getY(), dp1.getZ());
    DirectPosition dp4 = new DirectPosition(dp1.getX(), dp3.getY(), dp1.getZ());

    IDirectPosition dpcentre = b.getCenter();
    dpcentre.setZ(dp1.getZ());

    double distance = dpcentre.distance(dp1);

    dpcentre.setZ(dpcentre.getZ() + Math.tan(Math.toRadians(angle)) * distance);

    DirectPositionList dpl1 = new DirectPositionList();
    dpl1.add(dp1);
    dpl1.add(dp2);
    dpl1.add(dpcentre);
    dpl1.add(dp1);

    DirectPositionList dpl2 = new DirectPositionList();
    dpl2.add(dp2);
    dpl2.add(dp3);
    dpl2.add(dpcentre);
    dpl2.add(dp2);

    DirectPositionList dpl3 = new DirectPositionList();

    dpl3.add(dp3);
    dpl3.add(dp4);
    dpl3.add(dpcentre);
    dpl3.add(dp3);

    DirectPositionList dpl4 = new DirectPositionList();
    dpl4.add(dp4);
    dpl4.add(dp1);
    dpl4.add(dpcentre);
    dpl4.add(dp4);

    List<IOrientableSurface> lOS = new ArrayList<IOrientableSurface>(4);
    lOS.add(new GM_Polygon(new GM_LineString(dpl1)));
    lOS.add(new GM_Polygon(new GM_LineString(dpl2)));
    lOS.add(new GM_Polygon(new GM_LineString(dpl3)));
    lOS.add(new GM_Polygon(new GM_LineString(dpl4)));

    Shape3D shape = null;

    if (angle == this.angleMax) {
      shape = new Shape3D(ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lOS).getGeometryArray(),
          Prospect.genereApparence(true, Color.red, 0.3, true));
    } else if (angle == this.angleMin) {

      shape = new Shape3D(ConversionJava3DGeOxygene
          .fromOrientableSToTriangleArray(lOS).getGeometryArray(),
          Prospect.genereApparence(true, Color.green, 0.3, true));
    }

    bg.addChild(shape);

    return bg;
  }

}

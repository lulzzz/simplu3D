package fr.ign.cogit.simplu3d.exec.analysis;

import java.util.List;

import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.impl.Cuboid;
import fr.ign.cogit.simplu3d.rjmcmc.cuboid.geometry.loader.LoaderCuboid2;

public class StatsCuboidFromShapeFile {

  /**
   * @param args
   */
  public static void main(String[] args) {
    String configPath = "C:/Users/mbrasebin/Desktop/Exp1/test1BatBloc/result_1643897   .shp";
    List<Cuboid> lCuboid = LoaderCuboid2.loadFromShapeFile(configPath);
    
      
    for(Cuboid c : lCuboid){
     
      System.out.println(c.centerx + "," + c.centery + "," + Math.max(c.width, c.length) + "," + Math.min(c.width, c.length) + "," + c.height + "," + c.orientation);
      
    
      
    }

    

  }

}

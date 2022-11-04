package dao;

import model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DomicilioDAOH2 implements IDao<Domicilio>{
    private static final String SQL_INSERT="INSERT INTO DOMICILIOS (CALLE," +
            "NUMERO,LOCALIDAD,PROVINCIA) VALUES (?,?,?,?)";
    private static final String SQL_UPDATE="UPDATE DOMICILIOS SET CALLE=?, NUMERO=?, LOCALIDAD=?, PROVINCIA=? " +
            "WHERE ID=?";
    private static final String SQL_DELETE="DELETE FROM DOMICILIOS WHERE ID=?";
    private static final String SQL_SELECT_ALL="SELECT * FROM DOMICILIOS";
    private static final String SQL_SELECT="SELECT * FROM DOMICILIOS WHERE ID=?";
    private static final Logger LOGGER= Logger.getLogger(DomicilioDAOH2.class);
    @Override
    public Domicilio guardar(Domicilio domicilio) {
        Connection connection=null;
        try{
            LOGGER.info("Inicio de una operación de guardar de domicilio");
            connection=BD.getConnection();
            PreparedStatement psInsert=connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1,domicilio.getCalle());
            psInsert.setInt(2,domicilio.getNumero());
            psInsert.setString(3, domicilio.getLocalidad());
            psInsert.setString(4, domicilio.getProvincia());
            psInsert.execute();
            ResultSet rs=psInsert.getGeneratedKeys();
            while(rs.next()){
                domicilio.setId(rs.getInt(1));
            }

            //psInsert.close();
            //el código para guardar
        }
        catch (Exception e){
            LOGGER.error("Error: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try{
                connection.close();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return domicilio;
    }

    @Override
    public Domicilio buscar(Integer id) {
        Connection connection=null;
        LOGGER.info("Iniciando la busqueda de un domicilio con id="+id);
        Domicilio domicilio=null;
        try{
            connection=BD.getConnection();
            PreparedStatement psSelect=connection.prepareStatement(SQL_SELECT);
            psSelect.setInt(1,id);
            ResultSet rs=psSelect.executeQuery();
            while(rs.next()){
                //completar el domicilio
                domicilio=new Domicilio(rs.getInt(1),rs.getString(2),
                        rs.getInt(3),rs.getString(4),rs.getString(5));
            }
        }
        catch (Exception e){
            LOGGER.error("Error: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try{
                connection.close();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return domicilio;
    }

    @Override
    public void actualizar(Domicilio domicilio) {
        LOGGER.info("Iniciando la actualización de un domicilio");
        Connection connection=null;
        try{
            connection=BD.getConnection();
            PreparedStatement psUpdate=connection.prepareStatement(SQL_UPDATE);
            psUpdate.setString(1, domicilio.getCalle());
            psUpdate.setInt(2,domicilio.getNumero());
            psUpdate.setString(3, domicilio.getLocalidad());
            psUpdate.setString(4, domicilio.getProvincia());
            psUpdate.setInt(5,domicilio.getId());
            psUpdate.execute();
        }
        catch (Exception e){
            LOGGER.error("Error: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try{
                connection.close();
            }
            catch (SQLException es){
                es.printStackTrace();
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        Connection connection=null;
        LOGGER.info("Iniciando eliminación del domicilio con id="+id);
        try{
            connection=BD.getConnection();
            PreparedStatement psDelete=connection.prepareStatement(SQL_DELETE);
            psDelete.setInt(1,id);
            psDelete.execute();
            LOGGER.warn("Cuidado: Se borró el domicilio con id="+id);
        }
        catch (Exception e){
            LOGGER.error("Error: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try{
                connection.close();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<Domicilio> buscarTodo() {
        Connection connection=null;
        LOGGER.info("Iniciando la busqueda de todos los domicilios");
        List<Domicilio> domicilios= new ArrayList<>();
        Domicilio domicilio=null;
        try{
            connection=BD.getConnection();
            PreparedStatement psSelectAll=connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs=psSelectAll.executeQuery();
            while(rs.next()){
                //completar el domicilio
                domicilio=new Domicilio(rs.getInt(1),rs.getString(2),
                        rs.getInt(3),rs.getString(4),rs.getString(5));
                //incorporar el domicilio
                domicilios.add(domicilio);
            }
        }
        catch (Exception e){
            LOGGER.error("Error: "+e.getMessage());
            e.printStackTrace();
        }
        finally {
            try{
                connection.close();
            }
            catch (SQLException ex){
                ex.printStackTrace();
            }
        }
        return domicilios;
    }
}

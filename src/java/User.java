import java.sql.*;
import java.util.*;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import jdk.nashorn.tools.ShellFunctions.*;
//import static org.apache.jasper.tagplugins.jstl.core.Out.output;

@ManagedBean
@RequestScoped
public class User {

    int id;
    int cc;
    
    String name;
    String estado;
    
    String dates;
    String origentramite;
    
    String email;
    String password;
    
    String gender;
    String address;
    
    ArrayList usersList;
    
    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    
    Connection connection;

    
    
    public String getorigentramite() {
        return origentramite;
    }

    public void setorigentramite(String origentramite) {
        this.origentramite = origentramite;
    }

    
    
    public String getestado() {
        return estado;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }

    
    
    public String getdates() {
        return dates;
    }

    public void setdates(String dates) {
        this.dates = dates;
    }

    
    
    public int getcc() {
        return cc;
    }

    public void setcc(int cc) {
        this.cc = cc;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    
    
    
    
    public Connection getConnection() {
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            //Desarrollo
            //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user", "root", "");
            
            //Produccion
            connection = DriverManager.getConnection("jdbc:mysql://ec2-184-72-146-193.compute-1.amazonaws.com:3306/user", "root", "UniRemington2020");
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return connection;
    }

    
    
    
    public ArrayList ListaDeUsuarios() {
        
        try {
            
            usersList = new ArrayList();
            connection = getConnection();
            
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from users");
            
            while (rs.next()) {
                
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));
                usersList.add(user);
                
            }
            
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return usersList;
    }

    
    
    public ArrayList listadoDeSolicitudes() { 
        
        try {
            
            usersList = new ArrayList();
            connection = getConnection();
            
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from solicitudes");
            
            while (rs.next()) {
                
                User user = new User();
                user.setcc(rs.getInt("cedula"));
                user.setName(rs.getString("nombre"));
                user.setEmail(rs.getString("correo"));
                user.setdates(rs.getString("fechanacimiento"));
                user.setestado(rs.getString("estadosolicitud"));
                user.setorigentramite(rs.getString("origentramite"));

                usersList.add(user);
            }
            
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return usersList;
    }

    
    
    public ArrayList listadoDeSolicitudesCliente(String correo) {
        
        try {
            
            usersList = new ArrayList();
            connection = getConnection();
            
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from solicitudes where cedula=" + correo);
            
            while (rs.next()) {
                
                User user = new User();
                user.setcc(rs.getInt("cedula"));
                user.setName(rs.getString("nombre"));
                user.setEmail(rs.getString("correo"));
                user.setdates(rs.getString("fechanacimiento"));
                user.setestado(rs.getString("estadosolicitud"));
                user.setorigentramite(rs.getString("origentramite"));

                usersList.add(user);
            }
            
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return usersList;
    }

    
    
    
    private String output;

    public void contrasena() {
        output = "Error de inicio de sección, usuario sin registrar o contraseña incorrectos";
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    
    
    
    
     
    public String login(String correo, String contraseña) {
        
        User user = null;
        
        if (correo.equals("admin") && (contraseña.equals("admin"))) {
            
            return "/Administracion.xhtml?faces-redirect=true";
            
        } else {
            
            try {
                
                usersList = new ArrayList();
                connection = getConnection();
                
                Statement stmt = getConnection().createStatement();
                String X = "Select name from users where email='" + correo + "' and password='" + contraseña + "'";
                ResultSet rs = stmt.executeQuery("Select id,name from users where email='" + correo + "' and password='" + contraseña + "'");
                
                while (rs.next()) {
                    
                    int idcliente = -1;
                    idcliente = rs.getInt(1);
                    listadosolicitudes(idcliente);
                    return "/LSolicitudesCliente.xhtml?faces-redirect=true";
                }
                
            } catch (Exception e) {
                
                System.out.println(e);
                
            }
        }
        
        contrasena();
        return null;

    }

    
    
    
    public String guardarRegistro() {
        
        int result = 0;
        
        try {
            
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into users(name,email,password,gender,address) values(?,?,?,?,?)");
            
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, gender);
            stmt.setString(5, address);
            
            result = stmt.executeUpdate();
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        if (result != 0) {
            
            return "index.xhtml?faces-redirect=true";
            
        } else {
            
            return "Registrarse?faces-redirect=true";
            
        }
    }

    
    
    public String solicitarTarjetaDeCredito() {
        
        int result = 0;
        
        try {
            
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into solicitudes(cedula,nombre,correo,fechanacimiento,origentramite,estadosolicitud) values(?,?,?,?,?,?)");
            
            stmt.setInt(1, cc);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, dates);
            stmt.setString(5, origentramite);
            stmt.setString(6, "Pendiente");
            
            result = stmt.executeUpdate();
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        if (result != 0) {
            
            return "LSolicitudesCliente.xhtml?faces-redirect=true";
            
        } else {
            
            return "SolicitarTarjetaDeCredito.xhtml";
            
        }
    }

    
    
  
    public String edit(int id) {
        
        User user = null;
        
        try {
            
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select * from users where id = " + (id));
            rs.next();
            
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setGender(rs.getString("gender"));
            user.setAddress(rs.getString("address"));
            user.setPassword(rs.getString("password"));
            
            sessionMap.put("editUser", user);
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        return "/EditarCliente.xhtml?faces-redirect=true";
    }

    
    
    public String editatsolicitud(int id) {
        
        User user = null;
        
        try {

            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select * from solicitudes where cedula = " + (id));
            rs.next();
            
            user = new User();
            user.setcc(rs.getInt("cedula"));
            user.setName(rs.getString("nombre"));
            user.setEmail(rs.getString("correo"));
            user.setdates(rs.getString("fechanacimiento"));
            user.setorigentramite(rs.getString("origentramite"));
            user.setestado(rs.getString("estadosolicitud"));

            sessionMap.put("editUser", user);
            connection.close();
            
            return "/EstadoDeLaSolicitud.xhtml";
            
        } catch (Exception e) {
            
            System.out.println(e);

        }
        
        return null;
    }

    
    
    public String listadosolicitudes(int id) {
        
        User user = null;
        System.out.println(id);
        
        try {
            
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select name,email from users where id = " + (id));
            rs.next();
            
            user = new User();
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            
            sessionMap.put("user", user);
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return "/LSolicitudesCliente.xhtml"; 
    }

    
    
    
    public String nuevaTarjetaDeCredito(String correocliente) {
        
        User user = null;
        System.out.println(correocliente);
        
        try {
            
            connection = getConnection();
            Statement stmt = getConnection().createStatement();
            
            ResultSet rs = stmt.executeQuery("select name,email from users where correo = " + (correocliente));
            rs.next();
            
            user = new User();
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            
            sessionMap.put("user", user);
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return "/SolicitarTarjetaDeCredito.xhtml?faces-redirect=true"; 
    }

    
    
    
    public String update(User u) {
        
        
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("update users set name=?,email=?,password=?,gender=?,address=? where id=?");
            
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getGender());
            stmt.setString(5, u.getAddress());
            stmt.setInt(6, u.getId());
            stmt.executeUpdate();
            
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
        }
        
        return "/index.xhtml?faces-redirect=true";
    }

    
    
    public String actualizarSolicitudDeTarjetaDeCredito(User u) {
        
        
        try {
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("update solicitudes set estadosolicitud=? where cedula=?");
            
            stmt.setString(1, u.getestado());
            stmt.setInt(2, u.getcc());
            stmt.executeUpdate();
            
            connection.close();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
        
        return "/ListadoDeSolicitudes.xhtml?faces-redirect=true";
    }
 
    
    
   
    public void delete(int id) {
        
        try {
            
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("delete from users where id = " + id);
            stmt.executeUpdate();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
    }

    
    
    public void deletesolicitud(int cc) {
        
        try {
            
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("delete from solicitudes where cedula = " + cc
                    + " and estadosolicitud= 'Rechazada'");
            
            stmt.executeUpdate();
            
        } catch (Exception e) {
            
            System.out.println(e);
            
        }
    }

    
    
    
    public String getGenderName(char gender) {
        
        if (gender == 'M') {
            
            return "Masculino";
            
        } else {
            
            return "Femenino";
        }
        
    }

    
    
    public String getorigendeltramite(char origen) {
        
        if (origen == 'V') {
            
            return "Virtualmente";
            
        } else if (origen == 'T') {
            
            return "Telefonicamente";
            
        } else {
            
            return "En oficina";
            
        }
        
    }
 
}

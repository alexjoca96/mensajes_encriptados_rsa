/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensajes_encriptados;

import java.io.*;
import java.net.*;
import Servidor.Servidor;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.*;
public class Cliente {
 
    private static String HOST = "localhost";
    private static int PUERTO = 5050;
    static byte[] textoPlano, cifrado;
    static String txt="", aux;
//    public static KeyPairGenerator keygen;
//     public static KeyPair keypair;
    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, InvalidKeySpecException {
          BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Socket socket;
        DataOutputStream mensaje;
   try {
//creamos la clave usando el algoritmo AES con un tamaño de 128bits
        
     Cipher c = Cipher.getInstance("RSA");
     
        PrivateKey privateKey = leerLlavePrivada("privateKey");
        c.init(Cipher.ENCRYPT_MODE , privateKey);
        
            //Creamos nuestro socket
            socket = new Socket(HOST, PUERTO);
     
            //PrintWriter mensaje = new PrintWriter(socket.getOutputStream(),true);
            mensaje = new DataOutputStream(socket.getOutputStream());
            
        while(!txt.equalsIgnoreCase("fin")){
        System.out.println("Escribe el texto a encriptar");
        txt= br.readLine();
        //textoPlano= txt.getBytes("UTF8");
        cifrado= c.doFinal(txt.getBytes("UTF8"));
        System.out.println("Encriptado "+ new String(cifrado,"UTF8"));
        //aux=new String(cifrado,"UTF8");
            //Enviamos un mensaje
           // mensaje.println(aux);
            mensaje.writeInt(cifrado.length);
            mensaje.write(cifrado);
                    //mensaje.flush();
        }
            //Cerramos la conexión
            socket.close();
 
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println("Error de entrada/salida.");
        }
         
    }

    private static void saveKey(PrivateKey privateKey, String llavePrivada) throws FileNotFoundException, IOException {
         byte[] privateKeysBytes = privateKey.getEncoded();
         FileOutputStream fos = new FileOutputStream(llavePrivada);
         fos.write(privateKeysBytes);
         fos.close();
    }
      private static PrivateKey leerLlavePrivada(String fichero) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fis= new FileInputStream(fichero);
        int numBytes = fis.available();
        byte[] bytes = new byte[numBytes];
        fis.read(bytes);
        fis.close();
        
        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        PrivateKey keyfromBytes =keyFactory.generatePrivate(keySpec);
        return keyfromBytes;
    }
}

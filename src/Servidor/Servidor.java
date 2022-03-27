/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.*;
import mensajes_encriptados.Cliente;
public class Servidor {
    private static int PUERTO = 5050;
 
    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, InvalidKeySpecException {
         
       // BufferedReader entrada;
        DataOutputStream salida;
        Socket socket;
        ServerSocket serverSocket;
      
       try {  
//creamos la clave usando el algoritmo AES con un tamaño de 128bits
         KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
         KeyPair keypair = keygen.generateKeyPair();
         PrivateKey privateKey = keypair.getPrivate();
         PublicKey publicKey = keypair.getPublic();
         saveKey (privateKey,"privateKey");
            Cipher c = Cipher.getInstance("RSA");
        
       
            
            serverSocket = new ServerSocket(PUERTO);
 
            System.out.println("Esperando una conexión...");
 
            socket = serverSocket.accept();
 
            System.out.println("Un cliente se ha conectado...");
 
            // Para los canales de entrada y salida de datos
 
//            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// 
//            salida = new DataOutputStream(socket.getOutputStream());
 
 //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
 DataInputStream entrada = new DataInputStream(socket.getInputStream());
            
            System.out.println("Confirmando conexion al cliente....");
            String mensajeRecibido = "";
            int tamano = 0;
            c.init(Cipher.DECRYPT_MODE,publicKey);
            while(!mensajeRecibido.equals("fin")){
 
            // Para recibir el mensaje
            tamano= entrada.readInt();
            if(tamano >0){
                byte[] msg = new byte[tamano];
                entrada.readFully(msg, 0 , msg.length);
                System.out.println(msg);
            
            
            
            byte[] mensajeDescifrado = c.doFinal(msg);
            String mensajeDescifrado2 = new String(mensajeDescifrado);
            System.out.println(mensajeDescifrado2);
            
            }
            
            
       }
 
            System.out.println("Cerrando conexión...");
 
            // Cerrando la conexón
            serverSocket.close();
 
        } catch (IOException e) {
            System.out.println("Error de entrada/salida."  + e.getMessage());
        }
 
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
  private static void saveKey(PrivateKey privateKey, String llavePrivada) throws FileNotFoundException, IOException {
         byte[] privateKeysBytes = privateKey.getEncoded();
         FileOutputStream fos = new FileOutputStream(llavePrivada);
         fos.write(privateKeysBytes);
         fos.close();
    }
}
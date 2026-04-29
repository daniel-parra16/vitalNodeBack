package com.daniel_parra16.vitalNode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VitalNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitalNodeApplication.class, args);
	}

	/* 
	Quiero un paso a paso completo para configurar GitHub en Windows usando Git Bash, desde cero, para mi usuario:

usuario GitHub: daniel-parra16
correo: danielestebanparraruiz22@gmail.com

Necesito que me expliques cómo crear y configurar correctamente:

Una SSH Authentication key (para push/pull con GitHub)
Una SSH Signing key (para que los commits salgan como “Verified”)

Quiero que me des todos los comandos exactos para Windows + Git Bash, sin usar PowerShell variables como $env:USERPROFILE, solo rutas correctas para Git Bash.

También quiero que incluyas:

Cómo generar ambas claves correctamente
Cómo copiarlas y subirlas a GitHub paso a paso
Dónde elegir “Authentication key” vs “Signing key” en GitHub
Cómo configurar git con user.name, user.email
Cómo activar commits firmados automáticamente
Cómo verificar que todo funciona (comandos de prueba)
Cómo comprobar que los commits salen como “Verified” en GitHub

Si hay errores comunes en Windows o Git Bash, también explícalos y cómo evitarlos.




JWT.secret = "a9f3c2d8b7e64f1c9d2a5e8f3b6c1d9e7a4f0c2b8d6e1f9a3c5b7d2e8f4c6a1"
JWT.expiration.access=900000
JWT.expiration.refresh=604800000
JWT.inactividad.minutos=30
	*/

}

package com.hulkstore.springboot.web.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadsFileServiceImpl implements IUploadFileService {

	private final static String UPLOADS_FOLDER = "uploads";

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Resource load(String filename) throws MalformedURLException {

		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + " " + pathFoto);
		Resource recurso = null;

		recurso = new UrlResource(pathFoto.toUri());
		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());

		}
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		// la ruta seria uploads y concatenamos el nombre del archivo
		Path rootPath = getPath(uniqueFilename);

		log.info("rootPath: " + rootPath);

		// comvertimos la imagen en bytes
		byte[] bytes = file.getBytes();
		// asignamos a una variable la ruta completa mas el fombre original de la foto
		// Path rutaCompleta = Paths.get(rooPath + "//" + foto.getOriginalFilename());
		// pasamos la ruta y escribimos los bytes en la misma
		Files.write(rootPath, bytes);

		return uniqueFilename;
	}

	@Override
	public boolean delet(String filename) {
		Path rooPath = getPath(filename);
		File archivo = rooPath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {

		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deletAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());

	}

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));

	}

}

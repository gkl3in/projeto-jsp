package servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.codec.binary.Base64;

import beans.BeanCursoJsp;
import dao.DaoUsuario;

@WebServlet("/salvarUsuario")
@MultipartConfig
public class Usuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoUsuario daoUsuario = new DaoUsuario();

	public Usuario() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String acao = request.getParameter("acao");
			String user = request.getParameter("user");

			if ("delete".equalsIgnoreCase(acao)) {

				daoUsuario.delete(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("editar".equalsIgnoreCase(acao)) {

				BeanCursoJsp beanCursoJsp = daoUsuario.consultar(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("user", beanCursoJsp);
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("listartodos".equalsIgnoreCase(acao)) {

				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);
			} else if ("download".equalsIgnoreCase(acao)) {

				BeanCursoJsp beanCursoJsp = daoUsuario.consultar(user);

				if (beanCursoJsp != null) {
					
					String contentType = "";
					byte[] fileBytes = null;
					String tipo = request.getParameter("tipo");
					
					if ("imagem".equalsIgnoreCase(tipo)) {
						contentType = beanCursoJsp.getContentType();
						fileBytes = new Base64().decodeBase64(beanCursoJsp.getFotoBase64());
					} else if ("curriculo".equalsIgnoreCase(tipo)) {
						contentType = beanCursoJsp.getContentTypeCurriculo();
						fileBytes = new Base64().decodeBase64(beanCursoJsp.getCurriculoBase64());
					}

					response.setHeader("Content-Disposition", "attachment;filename=arquivo." + contentType.split("\\/")[1]);
					
					OutputStream os = response.getOutputStream();
					
					os.write(fileBytes);
					os.flush();
					os.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	@SuppressWarnings("static-access")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String acao = request.getParameter("acao");

		if (acao != null && acao.equalsIgnoreCase("reset")) {
			try {
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String id = request.getParameter("id");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");

			BeanCursoJsp usuario = new BeanCursoJsp();
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setId((id != null && !id.isEmpty()) ? Long.parseLong(id) : null);
			usuario.setNome(nome);
			usuario.setFone(request.getParameter("fone"));
			usuario.setBairro(request.getParameter("bairro"));
			usuario.setRua(request.getParameter("rua"));
			usuario.setCidade(request.getParameter("cidade"));
			usuario.setCep(request.getParameter("cep"));
			usuario.setEstado(request.getParameter("uf"));
			usuario.setSexo(request.getParameter("sexo"));
			
			if (request.getParameter("ativo") != null 
					&& request.getParameter("ativo").equalsIgnoreCase("on")) {
				usuario.setAtivo(true);
			} else {
				usuario.setAtivo(false);
			}

			try {
				
				/* In�cio upload de imagens */
				if (ServletFileUpload.isMultipartContent(request)) {

					Part imagemFoto = request.getPart("foto");
					
					if (imagemFoto != null && imagemFoto.getInputStream().available() > 0) {

						String fotoBase64 = new Base64().encodeBase64String(converteStreamParaByte(imagemFoto.getInputStream()));
						
						usuario.setFotoBase64(fotoBase64);
						usuario.setContentType(imagemFoto.getContentType());
						
						// Miniatura da imagem
						byte[] imagemByteDecode = new Base64().decodeBase64(fotoBase64);
						BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByteDecode));
						
						int tipoImagem = bufferedImage.getType() == 0 ? bufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
						
						BufferedImage resizedImage = new BufferedImage(100, 100, tipoImagem);
						Graphics2D g = resizedImage.createGraphics();
						g.drawImage(bufferedImage, 0, 0, 100, 100, null);
						g.dispose();

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(resizedImage, "png", baos);
						
						String miniaturaBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
						
						usuario.setFotoBase64Miniatura(miniaturaBase64);

					} else {
						usuario.setAtualizarImage(false);
					}
					
					Part curriculoPdf = request.getPart("curriculo");

					if (curriculoPdf != null && curriculoPdf.getInputStream().available() > 0) {
						String curriculoBase64 = new Base64().
								encodeBase64String(converteStreamParaByte(curriculoPdf.getInputStream()));
						
						usuario.setCurriculoBase64(curriculoBase64);
						usuario.setContentTypeCurriculo(curriculoPdf.getContentType());
					} else {
						usuario.setAtualizarPdf(false);
					}
				}
				/* fim upload de imagens */

				if (login == null || login.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "Login deve ser informado");
				} else if (senha == null || senha.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "senha deve ser informada");
				} else if (nome == null || nome.isEmpty()) {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "nome deve ser informado");
				} else if ((id == null || id.isEmpty()) && daoUsuario.validarLogin(login)) {
					daoUsuario.salvar(usuario);
					request.setAttribute("msg", "Usu�rio " + usuario.getNome() + " cadastrado com sucesso!");
				} else if ((id != null && !id.isEmpty()) && (daoUsuario.validarLogin(login)
						|| daoUsuario.consultar(id).getLogin().equalsIgnoreCase(login))) {
					daoUsuario.atualizar(usuario);
					request.setAttribute("msg", "Usu�rio alterado com sucesso!");
				} else {
					request.setAttribute("user", usuario);
					request.setAttribute("msg", "Login j� existente");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("usuarios", daoUsuario.listarUsuarios());
				view.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private byte[] converteStreamParaByte(InputStream imagem) throws IOException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = imagem.read();

		while (reads != -1) {
			baos.write(reads);
			reads = imagem.read();
		}
		return baos.toByteArray();
	}
}

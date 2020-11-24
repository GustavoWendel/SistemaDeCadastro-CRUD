package curso.springboot.controller;

import curso.springboot.model.Telefone;
import curso.springboot.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@GetMapping(value = "/cadastroPessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoaObj", new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasIt);
		return modelAndView;
	}

	@PostMapping(value = "**/salvarPessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

		if(bindingResult.hasErrors()){
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
 			modelAndView.addObject("pessoas", pessoasIt);
			modelAndView.addObject("pessoaObj", pessoa);

			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()){
				msg.add(objectError.getDefaultMessage()); //Vem das anotações @NotEmpty e outras
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}

		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/cadastroPessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaObj", new Pessoa());

		return andView;
	}

	@GetMapping(value = "/listaPessoas")
	public ModelAndView pessoas() {

		ModelAndView andView = new ModelAndView("cadastro/cadastroPessoa");

		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaObj", new Pessoa());
		return andView;
	}

	@GetMapping("/editarPessoa/{idPessoa}")
	public ModelAndView editar(@PathVariable("idPessoa") Long idPessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoaObj", pessoa.get());

		return modelAndView;
	}

	@GetMapping("/removerPessoa/{idPessoa}")
	public ModelAndView excluir(@PathVariable("idPessoa") Long idPessoa) {

		pessoaRepository.deleteById(idPessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaObj", new Pessoa());
		return modelAndView;
	}

	@PostMapping("**/pesquisarPessoa")
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") String nomePesquisa){
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomePesquisa));
		modelAndView.addObject("pessoaObj", new Pessoa());
		return modelAndView;
	}

	@GetMapping("/telefones/{idPessoa}")
	public ModelAndView telefones(@PathVariable("idPessoa") Long idPessoa) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(idPessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idPessoa));

		return modelAndView;
	}

	@PostMapping("**/addFonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone,
									  @PathVariable("pessoaid") Long pessoaid){

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		telefone.setPessoa(pessoa);

		telefoneRepository.save(telefone);
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;
	}

	@GetMapping("/removerTelefone/{idTelefone}")
	public ModelAndView removerTelefone(@PathVariable("idTelefone") Long idTelefone) {

		Pessoa pessoa = telefoneRepository.findById(idTelefone).get().getPessoa();

		telefoneRepository.deleteById(idTelefone);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaObj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
		return modelAndView;
	}
}


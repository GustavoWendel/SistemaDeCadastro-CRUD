package curso.springboot.controller;

import curso.springboot.model.Telefone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;

import java.util.Optional;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@GetMapping(value = "/cadastroPessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastroPessoa");
		modelAndView.addObject("pessoaObj", new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasIt);
		return modelAndView;
	}

	@PostMapping(value = "**/salvarPessoa")
	public ModelAndView salvar(Pessoa pessoa) {

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

	@GetMapping("/excluirPessoa/{idPessoa}")
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

		return modelAndView;
	}

	@PostMapping("**/addFonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone,
									  @PathVariable("pessoaid") Long pessoaid){
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		return modelAndView;
	}
}


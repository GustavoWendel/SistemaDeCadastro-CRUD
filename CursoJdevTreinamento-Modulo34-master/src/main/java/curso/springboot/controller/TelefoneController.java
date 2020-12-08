package curso.springboot.controller;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TelefoneController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

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

        if(telefone != null && telefone.getNumero().isEmpty()
                            || telefone.getTipo().isEmpty()){

            ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
            modelAndView.addObject("pessoaObj", pessoa);
            modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));

            List<String> msg = new ArrayList<String>();
            if(telefone.getNumero().isEmpty()){
                msg.add("Número deve ser informado");
            }

            if(telefone.getTipo().isEmpty()){
                msg.add("Tipo deve ser informado");
            }
            modelAndView.addObject("msg", msg);

            return modelAndView;
        }

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

package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    // ACCEDE AL REPOSITORIO (BBDD), recupera datos o los guarda
    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, ClientDto dto) throws Exception {

        Client client;

        if (id == null) {  // Si id es nulo, creamos un nuevo cliente
            // Verificamos si ya existe un cliente con el mismo nombre
            if (clientRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Name already exists");
            }
            client = new Client();

        } else {  // Si el id no es nulo, buscamos el cliente en la base de datos para actualizarlo
            client = this.get(id);
        }

        // Mapeamos los datos del DTO al cliente
        client.setName(dto.getName());

        // Guardamos el cliente en la base de datos
        this.clientRepository.save(client);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.clientRepository.deleteById(id);
    }

}

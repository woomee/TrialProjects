package kajitool.web.service.material;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kajitool.web.domain.repository.MaterialRepository;
import kajitool.web.domain.model.Material;

@Service
@Transactional
public class MaterialService {
    private final MaterialRepository repository;

    public MaterialService(final MaterialRepository repository) {
        this.repository = repository;
    }
    public List<Material> findAll() {
        return repository.selectAll();
    }
}
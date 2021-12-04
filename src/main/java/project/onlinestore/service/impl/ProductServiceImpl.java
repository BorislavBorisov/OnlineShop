package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.SupplierService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, SupplierService supplierService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductViewModel> findAllProducts() {
        return this.productRepository
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel) {
        ProductEntity byName = this.productRepository.findByProductName(productServiceModel.getProductName())
                .orElse(null);

        ProductEntity byCode = this.productRepository.findByProductCode(productServiceModel.getProductName())
                .orElse(null);

        if (byName == null && byCode == null) {
            ProductEntity product = this.modelMapper.map(productServiceModel, ProductEntity.class);
            product.setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638364042/empty_kk164n.jpg");
            product.setProductNameLatin(translate(productServiceModel.getProductName()));
            return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
        }

        throw new IllegalArgumentException("Something went wrong!");
    }

    @Override
    public ProductServiceModel findProductById(Long id) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel editProduct(Long id, ProductServiceModel productServiceModel) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        product.setProductName(productServiceModel.getProductName())
                .setProductCode(productServiceModel.getProductCode())
                .setProductPrice(productServiceModel.getProductPrice())
                .setDescription(productServiceModel.getDescription())
                .setProductNameLatin(translate(productServiceModel.getProductName()))
                .setModified(Instant.now());

        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);

    }

    @Override
    public boolean deleteProduct(Long id) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        try {
            this.productRepository.delete(product);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean editImageCategory(ProductServiceModel productServiceModel) {
        ProductEntity product = this.productRepository.findById(productServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID!"));

        try {
            product.setImgUrl(productServiceModel.getImgUrl());
            product.setModified(Instant.now());
            this.productRepository.save(product);
            return true;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid product ID!");
        }
    }

    @Override
    public ProductServiceModel findProductByNameLatin(String nameLatin) {
        return this.modelMapper.map(
                this.productRepository.findByProductNameLatin(nameLatin).orElseThrow(() ->
                        new IllegalArgumentException("Invalid product")), ProductServiceModel.class
        );
    }

    @Override
    public ProductEntity findProductByName(String key) {
        return this.productRepository.findByProductName(key).orElse(null);
    }

    @Override
    public void seedProducts() {
        seedProducts15();
    }

    private void seedProducts15() {
        if (this.productRepository.count() == 0) {
            CategoryEntity computerCategory = this.modelMapper.map(
                    this.categoryService.findCategoryById(1L), CategoryEntity.class
            );

            SupplierEntity computerSupplier = this.modelMapper.map(
                    this.supplierService.findSupplierById(1L), SupplierEntity.class
            );

            ProductEntity product = new ProductEntity();
            product.setProductName("Turbo-X Erebus E5 Desktop (AMD Ryzen 5 3400G/8 GB/240 GB/Radeon RX Vega 11)");
            product.setProductNameLatin(translate(product.getProductName()))
                    .setProductCode("pc-1")
                    .setProductPrice(BigDecimal.valueOf(1149.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638467247/3726177_k0yyd3.jpg")
                    .setDescription("Erebus E5 с процесор AMD Ryzen™ 5 3400G, 8GB DDR4 RAM на 2400MHz, SSD 240GB и HDD 1TB, интерфейс на свързване Wi-Fi/Bluetooth и операционна система Windows 10!")
                    .setCategory(computerCategory)
                    .setSupplier(computerSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product1 = new ProductEntity();
            product1.setProductName("Turbo-X Gaming Set Nemesis Powered by Asus");
            product1.setProductNameLatin(translate(product1.getProductName()))
                    .setProductCode("pc-2")
                    .setProductPrice(BigDecimal.valueOf(6998.99))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638467247/3818993_pgue2a.jpg")
                    .setDescription("Gaming Set с Turbo-X Nemesis Powered by ASUS Gaming Desktop и монитор ASUS 27\" VG279Q1A TUF Gaming, ROG Delta S, RX ROG STRIX Scope, ROG Gladius III, ROG Scabbard II и RT-AX88U Gaming Router!")
                    .setCategory(computerCategory)
                    .setSupplier(computerSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product2 = new ProductEntity();
            product2.setProductName("Turbo-X Paladin Student SR Desktop (Intel Quad Core/4 GB/128 GB/GT 1030 2 GB)");
            product2.setProductNameLatin(translate(product2.getProductName()))
                    .setProductCode("pc-3")
                    .setProductPrice(BigDecimal.valueOf(649.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637776111/ahwo6oxq7eymhhugl4w3.jpg")
                    .setDescription("С процесор Intel® Celeron® J4125 на 2,0GHz, видеокарта NVIDIA® GeForce® GT 1030 2GB, 4GB RAM DDR4-2400, SSD 128GB и Windows 10 Home (S Mode)!")
                    .setCategory(computerCategory)
                    .setSupplier(computerSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product3 = new ProductEntity();
            product3.setProductName("Turbo-X Nemesis N2761 Desktop (Intel Core i5 12600K/16 GB/250 GB/RTX 3070 8 GB)");
            product3.setProductNameLatin(translate(product3.getProductName()))
                    .setProductCode("pc-41")
                    .setProductPrice(BigDecimal.valueOf(4399.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637775396/l7e72xvok6aefo1h4yov.jpg")
                    .setDescription("Очаква се! Gaming PC с дъно Intel® Z690, 10-ядрен процесор Intel® Core™ i5-12600K 12-то поколение, 16GB памет DDR5-5200, NVMe™ SSD 250GB + 2TB HDD, NVIDIA® GeForce RTX™ 3070 и Windows 11!")
                    .setCategory(computerCategory)
                    .setSupplier(computerSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product4 = new ProductEntity();
            product4.setProductName("Turbo-X Erebus E2020 Desktop (AMD Ryzen 5 3400G/16 GB/240GB/RTX 2060 6 GB)");
            product4.setProductNameLatin(translate(product4.getProductName()))
                    .setProductCode("pc-4")
                    .setProductPrice(BigDecimal.valueOf(1149.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637776088/i1kmfggowxr5z6fq6ljk.jpg")
                    .setDescription("Erebus E2020 с процесор AMD Ryzen™ 5 3400G, 16GB RAM DDR4, видеокарта NVIDIA GeForce® RTX™ 2060 с 6GB памет, SSD 240GB + HDD 1TB и Windows 11!")
                    .setCategory(computerCategory)
                    .setSupplier(computerSupplier)
                    .setRegistered(Instant.now());

            this.productRepository.saveAll(List.of(product, product1, product2, product3, product4));

            CategoryEntity laptopCategory = this.modelMapper.map(
                    this.categoryService.findCategoryById(2L), CategoryEntity.class
            );

            SupplierEntity laptopSupplier = this.modelMapper.map(
                    this.supplierService.findSupplierById(3L), SupplierEntity.class
            );

            ProductEntity product5 = new ProductEntity();
            product5.setProductName("Acer Aspire 3 A315-23-R1F4");
            product5.setProductNameLatin(translate(product5.getProductName()))
                    .setProductCode("lp-1")
                    .setProductPrice(BigDecimal.valueOf(1049.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638468848/55f85a0bc59de14b605dd2665277c8cb6179365b95ec2.3852652_gpudlh.jpg")
                    .setCategory(laptopCategory)
                    .setSupplier(laptopSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product6 = new ProductEntity();
            product6.setProductName("HUAWEI MateBook D15 Laptop (Core i3 1005G1/8 GB/256 GB/UHD Graphics 620)");
            product6.setProductNameLatin(translate(product6.getProductName()))
                    .setProductCode("lp-2")
                    .setProductPrice(BigDecimal.valueOf(1654.99))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638468848/3778827_jdq18j.jpg")
                    .setDescription("С елегантна, лека и метална конструкция, процесор Intel® Core™ D15 i3-1005G1, 8GB DDR4 RAM, NVMe SSD с вместимост 256GB и дисплей 15,6\" с Full HD резолюция!")
                    .setCategory(laptopCategory)
                    .setSupplier(laptopSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product7 = new ProductEntity();
            product7.setProductName("Lenovo IdeaPad 3 15IIL05");
            product7.setProductNameLatin(translate(product7.getProductName()))
                    .setProductCode("lp-3")
                    .setProductPrice(BigDecimal.valueOf(1029.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638468848/55f85a0bc59de14b605dd2665277c8cb6179365b95ec2.3852652_gpudlh.jpg")
                    .setDescription("С процесор Intel® Celeron® J4125 на 2,0GHz, видеокарта NVIDIA® GeForce® GT 1030 2GB, 4GB RAM DDR4-2400, SSD 128GB и Windows 10 Home (S Mode)!")
                    .setCategory(laptopCategory)
                    .setSupplier(laptopSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product8 = new ProductEntity();
            product8.setProductName("Microsoft Surface Go 3 Laptop (Pentium Gold 6500Y/4 GB/64 GB/UHD Graphics 615)");
            product8.setProductNameLatin(translate(product8.getProductName()))
                    .setProductCode("lp-41")
                    .setProductPrice(BigDecimal.valueOf(4399.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638468848/3824357_zgraue.jpg")
                    .setDescription("Microsoft Surface Go 3 Laptop (Pentium Gold 6500Y/4 GB/64 GB/UHD Graphics 615)")
                    .setCategory(laptopCategory)
                    .setSupplier(laptopSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product9 = new ProductEntity();
            product9.setProductName("HP 250 G8 Black Win10Pro");
            product9.setProductNameLatin(translate(product9.getProductName()))
                    .setProductCode("lp-4")
                    .setProductPrice(BigDecimal.valueOf(999.99))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638468848/e4f7ff303d2e18cbf8dcf2e100c1b91f6179148325872.3851680_omfgvk.jpg")
                    .setDescription("HP 250 G8 Black Win10Pro")
                    .setCategory(laptopCategory)
                    .setSupplier(laptopSupplier)
                    .setRegistered(Instant.now());

            this.productRepository.saveAll(List.of(product5, product6, product7, product8, product9));

            CategoryEntity smartphoneCategory = this.modelMapper.map(
                    this.categoryService.findCategoryById(3L), CategoryEntity.class
            );

            SupplierEntity smartphoneSupplier = this.modelMapper.map(
                    this.supplierService.findSupplierById(4L), SupplierEntity.class
            );

            ProductEntity product10 = new ProductEntity();
            product10.setProductName("Xiaomi 11T 128GB 5G Smartphone Grey");
            product10.setProductNameLatin(translate(product10.getProductName()))
                    .setProductCode("sm-3")
                    .setProductPrice(BigDecimal.valueOf(989.00))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638469296/2e8e34318857aa4713af56f193d632286167dc4567006.3836134_mvxyls.jpg")
                    .setDescription("С дисплей AMOLED FHD+, процесор Mediatek 5G, камера 108MP, ultra wide и macro, както и батерия 5000mAh с 67W Mi Turbo Charge!")
                    .setCategory(smartphoneCategory)
                    .setSupplier(smartphoneSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product11 = new ProductEntity();
            product11.setProductName("Honor 50 6/128GB Emerald Green");
            product11.setProductNameLatin(translate(product11.getProductName()))
                    .setProductCode("sm-421")
                    .setProductPrice(BigDecimal.valueOf(877.33))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638469296/3845966_ufc59s.jpg")
                    .setDescription("Блести като диамант с HONOR 50, с четворна камера 108MP, 5G, дисплей OLED FHD+ 120 Hz, батерия 4300 mAh и бързо зареждане 66W!")
                    .setCategory(smartphoneCategory)
                    .setSupplier(smartphoneSupplier)
                    .setRegistered(Instant.now());

            ProductEntity product12 = new ProductEntity();
            product12.setProductName("Apple iPhone SE 64GB 4G Smartphone White");
            product12.setProductNameLatin(translate(product12.getProductName()))
                    .setProductCode("sm-4")
                    .setProductPrice(BigDecimal.valueOf(921.99))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638469296/3470326_hmwnph.jpg")
                    .setDescription("С обновена визия, мощен процесор A13 Bionic, най-добрата единична камера на Apple, дисплей 4,7\" Retina HD с технология True Tone, iPhone SE предлага една неповторима комбинация от технологии - запазвайки в същото време компактния си размер!\n" +
                            "\n" +
                            "Най-бързият чип, съществувал някога в смартфон. Батерия, която издържа цял ден, така че да правиш по-дълго това, което обичаш. Видео 4K при 60 кадъра в секунда и smart HDR от следващо поколение. Портретен режим, поддръжка на съдържание Dolby Vision/HDR10, достъп до Apple TV+ и Apple Arcade, най-добрите приложения от разширена реалност. Интерфейс на свързване Wi‑Fi 6. Home Button с усъвършенстван четец на пръстов отпечатък и Touch ID. Сертификат IP67 за по-голяма устойчивост, CarPlay и много, много други!\n" +
                            "\n" +
                            "По-мощен и по-достъпен от всякога, с iPhone SE имаш всичко необходимо, за да се наслаждаваш на преживяванията си по-добре от всякога!")
                    .setCategory(smartphoneCategory)
                    .setSupplier(smartphoneSupplier)
                    .setRegistered(Instant.now());

            this.productRepository.saveAll(List.of(product10, product12, product11));

        }

    }
}

const allProducts = [];
fetch("/fetch/products")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            allProducts.push(d);
        }
    });

function selectElement(selector) {
    return document.querySelector(selector);
}

function clearResults() {
    selectElement('#search-results').innerHTML = "";
}

function getResult() {
    const search = selectElement('.searchbar').value;

    clearResults();

    console.log(allProducts)
    if (search.length > 0) {
        for (let i = 0; i < allProducts.length; i++) {
            if (allProducts[i].productName.toLowerCase().includes(search.toLowerCase())) {
                $('#search-results').append(`
                <div class="col-md-4" >
                <div class="row justify-content-between px-4" >
                    <div class="text-center">
                     <a class="text-center" href="/shop/article/${allProducts[i].productNameLatin}">
                            <img src="${allProducts[i].imgUrl}" width="250"> </a>
                     </div>
                    <div class="text-center">
                        <h5>${allProducts[i].productName}</h5>
                        <span class="text-success">${allProducts[i].productPrice}</span>
                    </div>
                </div>
           </div>`)
            }
        }
    }
}

selectElement('.searchbar').addEventListener('keyup', getResult);

// selectElement('#search-results').innerHTML += `
//            <div class="col-md-4" >
//                 <div class="row justify-content-between px-4" >
//                     <div class="text-center">
//                      <a class="text-center" th:href="@{/shop/article/{nameLatin}(nameLatin=${allProducts[i].productNameLatin})}">
//                             <img th:src="${allProducts[i].imgUrl}" width="250"> </a>
//                      </div>
//                         <h1>ssss</h1>
//                     <div class="text-center">
//                         <h5 th:text="${allProducts[i].productName}"></h5>
//                         <span class="text-success" th:text="*{productPrice}"></span>
//                     </div>
//                 </div>
//            </div>`;
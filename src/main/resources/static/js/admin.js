// GLOBAL STATE
let categoryMode = "create";
let productMode = "create";
let selectedImage = null;

// NAVIGATION
document.querySelectorAll(".sidebar li[data-section]").forEach(item => {
    item.addEventListener("click", () => {
        document.querySelectorAll(".sidebar li").forEach(li => li.classList.remove("active"));
        document.querySelectorAll(".section").forEach(sec => sec.classList.remove("active"));

        item.classList.add("active");
        document.getElementById(item.dataset.section).classList.add("active");

        if (item.dataset.section === "categories") loadCategories();
        if (item.dataset.section === "products") loadProducts();
        if (item.dataset.section === "orders") loadOrders();
    });
});

// LOGOUT
document.querySelector(".logout").addEventListener("click", async () => {
    await fetch("/api/auth/logout", { method: "POST" });
    window.location.href = "/login";
});

// CATEGORIES
async function loadCategories() {
    const res = await fetch("/api/admin/category");
    const categories = await res.json();

    const table = document.getElementById("category-table");
    table.innerHTML = "";

    categories.forEach(cat => {
        table.innerHTML += `
            <tr>
                <td>${cat.name}</td>
                <td>${cat.description || ""}</td>
                <td>${cat.type || ""}</td>
                <td>
                    <button onclick="editCategory(${cat.id})">✏️</button>
                    <button onclick="deleteCategory(${cat.id})">🗑️</button>
                </td>
            </tr>
        `;
    });
}

function openCreateCategory() {
    categoryMode = "create";
    document.getElementById("categoryModalTitle").innerText = "Créer une catégorie";
    document.getElementById("cat-id").value = "";
    document.getElementById("cat-name").value = "";
    document.getElementById("cat-description").value = "";
    document.getElementById("cat-type").value = "";
    document.getElementById("categoryModal").style.display = "flex";
}

function editCategory(id) {
    categoryMode = "edit";

    fetch(`/api/admin/category/${id}`)
        .then(res => res.json())
        .then(cat => {
            document.getElementById("categoryModalTitle").innerText = "Modifier catégorie";
            document.getElementById("cat-id").value = cat.id;
            document.getElementById("cat-name").value = cat.name;
            document.getElementById("cat-description").value = cat.description || "";
            document.getElementById("cat-type").value = cat.type || "";
            document.getElementById("categoryModal").style.display = "flex";
        });
}

async function saveCategory() {
    const payload = {
        name: document.getElementById("cat-name").value,
        description: document.getElementById("cat-description").value,
        type: document.getElementById("cat-type").value
    };

    if (categoryMode === "create") {
        await fetch("/api/admin/category", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
    } else {
        const id = document.getElementById("cat-id").value;
        await fetch(`/api/admin/category/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
    }

    closeModal();
    loadCategories();
}

async function deleteCategory(id) {
    if (!confirm("Supprimer cette catégorie ?")) return;
    await fetch(`/api/admin/category/${id}`, { method: "DELETE" });
    loadCategories();
}

// PRODUCTS
async function loadProducts() {
    const res = await fetch("/api/admin/product");
    const products = await res.json();

    const table = document.getElementById("product-table");
    table.innerHTML = "";

    products.forEach(p => {
        table.innerHTML += `
            <tr>
                <td>${p.name}</td>
                <td>${p.price} €</td>
				<td>${p.stock}</td>
                <td>
                    <button onclick="editProduct(${p.id})">✏️</button>
                    <button onclick="deleteProduct(${p.id})">🗑️</button>
                </td>
            </tr>
        `;
    });
}

function openCreateProduct() {
    productMode = "create";

    document.getElementById("productModalTitle").innerText = "Créer un produit";

    document.getElementById("prod-id").value = "";
    document.getElementById("prod-name").value = "";
    document.getElementById("prod-description").value = "";
    document.getElementById("prod-price").value = "";
    document.getElementById("prod-stock").value = "";
    document.getElementById("prod-onsale").value = 0;
    document.getElementById("prod-image").value = "";

    loadCategoryOptions();
    document.getElementById("productModal").style.display = "flex";
}

function editProduct(id) {
    productMode = "edit";

    fetch(`/api/admin/product/${id}`)
        .then(res => res.json())
        .then(p => {
            document.getElementById("productModalTitle").innerText = "Modifier produit";

            document.getElementById("prod-id").value = p.id;
            document.getElementById("prod-name").value = p.name;
            document.getElementById("prod-description").value = p.description;
            document.getElementById("prod-price").value = p.price;
            document.getElementById("prod-stock").value = p.stock;
            document.getElementById("prod-onsale").value = p.onSale;

            loadCategoryOptions(p.categoryId);

            const preview = document.getElementById("image-preview");
            if (p.pathImage) {
                preview.src = p.pathImage;
                preview.style.display = "block";
            } else {
                preview.style.display = "none";
            }

            document.getElementById("prod-image").value = "";
            selectedImage = null;

            document.getElementById("productModal").style.display = "flex";
        });
}

async function saveProduct() {

    const formData = new FormData();

    formData.append("name", document.getElementById("prod-name").value);
    formData.append("description", document.getElementById("prod-description").value);
    formData.append("price", document.getElementById("prod-price").value);
    formData.append("stock", document.getElementById("prod-stock").value);
	formData.append(
	    "onSale",
	    parseInt(document.getElementById("prod-onsale").value || "0", 10)
	);
    formData.append("categoryId", document.getElementById("prod-category").value);

    if (selectedImage) {
        formData.append("image", selectedImage);
    }

    let url = "/api/admin/product";
    let method = "POST";

    if (productMode === "edit") {
        const id = document.getElementById("prod-id").value;
        url = `/api/admin/product/${id}`;
        method = "PUT";
    }

    await fetch(url, {
        method,
        body: formData
    });

    selectedImage = null;
    closeModal();
    loadProducts();
}

async function deleteProduct(id) {
    if (!confirm("Supprimer ce produit ?")) return;
    await fetch(`/api/admin/product/${id}`, { method: "DELETE" });
    loadProducts();
}

const dropZone = document.getElementById("drop-zone");
const fileInput = document.getElementById("prod-image");
const preview = document.getElementById("image-preview");

dropZone.addEventListener("click", () => fileInput.click());

dropZone.addEventListener("dragover", e => {
    e.preventDefault();
    dropZone.classList.add("hover");
});

dropZone.addEventListener("dragleave", () => {
    dropZone.classList.remove("hover");
});

dropZone.addEventListener("drop", e => {
    e.preventDefault();
    dropZone.classList.remove("hover");
    handleFile(e.dataTransfer.files[0]);
});

fileInput.addEventListener("change", () => {
    handleFile(fileInput.files[0]);
});

function handleFile(file) {
    if (!file || !file.type.startsWith("image/")) return;

    selectedImage = file;

    const reader = new FileReader();
    reader.onload = e => {
        preview.src = e.target.result;
        preview.style.display = "block";
    };
    reader.readAsDataURL(file);
}

// ORDERS
async function loadOrders() {
    const res = await fetch("/api/admin/order");
    const orders = await res.json();

    const table = document.getElementById("order-table");
    table.innerHTML = "";

    orders.forEach(o => {
        table.innerHTML += `
            <tr>
                <td>#${o.id}</td>
                <td>${o.userMail}</td>
                <td>${o.priceTotal} €</td>
                <td>
                    <select onchange="updateOrderStatus(${o.id}, this.value)">
                        ${renderStatusOptions(o.status)}
                    </select>
                </td>
                <td>${new Date(o.orderDate).toLocaleDateString()}</td>
            </tr>
        `;
    });
}

function renderStatusOptions(current) {
    return ["PENDING", "PAID", "SHIPPED", "CANCELLED"]
        .map(s => `<option value="${s}" ${s === current ? "selected" : ""}>${s}</option>`)
        .join("");
}

async function updateOrderStatus(id, status) {
    await fetch(`/api/admin/order/${id}/status`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ status })
    });
}

// MODAL
function closeModal() {
    document.querySelectorAll(".modal").forEach(m => m.style.display = "none");
}

async function loadCategoryOptions(selectedId = null) {
    const res = await fetch("/api/admin/category");
    const categories = await res.json();

    const select = document.getElementById("prod-category");
    select.innerHTML = "";

    categories.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat.id;
        option.textContent = cat.name;
        if (selectedId && cat.id === selectedId) {
            option.selected = true;
        }
        select.appendChild(option);
    });
}


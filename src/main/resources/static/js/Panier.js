export default class Panier {
    constructor() {
		this.orderId = JSON.parse(localStorage.getItem("orderId"));
        this.pannier = JSON.parse(localStorage.getItem("pannier")) || [
		            { productId: 1, name: "Ordinateur Gaming Ultra X", category: "Gaming", price: 1699.99, quantity: 1 },
		            { productId: 2, name: "Casque Audio Premium", category: "Audio", price: 129.99, quantity: 1 },
		            { productId: 3, name: "Clavier Mécanique RGB", category: "Accessoires", price: 89.99, quantity: 1 }
		        ];

				localStorage.setItem("panier", JSON.stringify(this.pannier));
        this.run();
    }

    run() {
		console.log("tee")
        this.initialEvent();
    }

    async initialEvent() {
        this.onClickButtonsChangeQuantity();
    }

    onClickButtonsChangeQuantity() {
        this.buttonsChangeQuantity = document.querySelectorAll("[data-action='reduce'], [data-action='add']");
		console.log(this.buttonsChangeQuantity);
        this.buttonsChangeQuantity.forEach(button => {
			console.log("nam", button?.name)
            button.addEventListener("click", (e) => this.onChangeQuantity(e));
        });
    }

    async onChangeQuantity(e) {
		console.log("eee")
        const button = e.currentTarget;
        const productId = parseInt(button.dataset.productId);
        const action = button.dataset.action;

        const item = this.pannier.find(p => p.productId === productId);

        if (!item) return;

        if (action === "reduce") {
            item.quantity += 1;
        } else if (action === "add" && item.quantity > 1) {
            item.quantity -= 1;
        }

        await this.modifyItem(productId, item.quantity);
    }

    async modifyItem(productId, quantity) {
        localStorage.setItem("panier", JSON.stringify(this.pannier));

        const response = await fetch(`/api/admin/order-items/${productId}?quantity=${quantity}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        });

        const data = await response.json();
        console.log(data);
    }
}

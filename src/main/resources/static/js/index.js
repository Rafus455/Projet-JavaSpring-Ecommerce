function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/[&<>"']/g, function (s) {
        return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"})[s];
    });
}

function renderProductCard(p) {
    const img = encodeURI(p.pathImage || p.image || p.imageUrl || '/images/poulpe.webp');
    const name = escapeHtml(p.name || 'Produit');
    const desc = escapeHtml(p.description || '');
    const price = (typeof p.price === 'number') ? p.price.toFixed(2) + ' €' : (p.price || '');
    const productUrl = '/produit/' + encodeURIComponent(p.id);
    return `
        <a class="item-link" href="${productUrl}">
            <div class="item-card">
                <div class="item-image"><img src="${img}" alt="${name}"></div>
                <div class="item-info">
                    <h3>${name}</h3>
                    <p>${desc}</p>
                    <div class="item-price">${price}</div>
                    <button class="add-to-cart" type="button">Ajouter au panier</button>
                </div>
            </div>
        </a>
    `;
}

function shuffle(arr) {
    for (let i = arr.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [arr[i], arr[j]] = [arr[j], arr[i]];
    }
}

document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/auth/me', { credentials: 'same-origin' })
        .then(r => {
            if (!r.ok) return null;
            return r.json();
        })
        .then(data => {
            if (!data) return;
            const role = data.role || '';
            if (typeof role === 'string' && role.toUpperCase().includes('ADMIN')) {
                const el = document.getElementById('admin-link');
                if (el) el.style.display = '';
            }
        })
        .catch(() => {});

    const nouveautesContainer = document.getElementById('nouveautes');
    const bestContainer = document.getElementById('best-items');

    fetch('/api/products/new?limit=3')
        .then(r => r.ok ? r.json() : Promise.reject(r))
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                nouveautesContainer.innerHTML = '<p>Aucun produit trouvé.</p>';
                return;
            }
            nouveautesContainer.innerHTML = data.map(renderProductCard).join('');
        })
        .catch(err => {
            console.error('Erreur chargement nouveautés', err);
            nouveautesContainer.innerHTML = '<p>Impossible de charger les nouveautés.</p>';
        });

    fetch('/api/products/best')
        .then(r => r.ok ? r.json() : Promise.reject(r))
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                bestContainer.innerHTML = '<p>Aucun produit trouvé.</p>';
                return;
            }
            shuffle(data);
            const slice = data.slice(0, 3);
            bestContainer.innerHTML = slice.map(renderProductCard).join('');
        })
        .catch(err => {
            console.error('Erreur chargement meilleures ventes', err);
            bestContainer.innerHTML = '<p>Impossible de charger les meilleures ventes.</p>';
        });
});

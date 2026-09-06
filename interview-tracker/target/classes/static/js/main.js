// Interview Tracker — Main JS

document.addEventListener('DOMContentLoaded', () => {
  initAlerts();
  initSearch();
  animateStats();
});

// Auto-dismiss alerts after 4s
function initAlerts() {
  document.querySelectorAll('.alert').forEach(alert => {
    setTimeout(() => {
      alert.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
      alert.style.opacity = '0';
      alert.style.transform = 'translateY(-8px)';
      setTimeout(() => alert.remove(), 400);
    }, 4000);
  });
}

// Live search debounce
function initSearch() {
  const input = document.getElementById('searchInput');
  if (!input) return;

  let timer;
  input.addEventListener('input', () => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      const form = input.closest('form');
      if (form) form.submit();
    }, 400);
  });
}

// Animate stat numbers counting up
function animateStats() {
  document.querySelectorAll('.stat-number').forEach(el => {
    const target = parseInt(el.textContent, 10);
    if (isNaN(target) || target === 0) return;

    let current = 0;
    const step = Math.max(1, Math.floor(target / 20));
    const interval = setInterval(() => {
      current = Math.min(current + step, target);
      el.textContent = current;
      if (current >= target) clearInterval(interval);
    }, 30);
  });
}

// Confirm quick status changes for detail page
document.querySelectorAll('select[name="status"]').forEach(sel => {
  const original = sel.value;
  sel.addEventListener('change', function () {
    const label = this.options[this.selectedIndex].text;
    if (!confirm(`Change status to "${label}"?`)) {
      this.value = original;
    }
  });
});

console.log("Home page loaded successfully.");

/* Search box function */
const searchButton = document.querySelector(".search-box button");
const searchInput = document.querySelector(".search-box input");

if (searchButton && searchInput) {
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();

        if (keyword === "") {
            alert("Please enter an artist, team, or venue name.");
        } else {
            alert("Searching for: " + keyword);
        }
    });

    searchInput.addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            searchButton.click();
        }
    });
}

/* Scroll 3D animation */
const scroll3dElements = document.querySelectorAll(".scroll-3d");

if (scroll3dElements.length > 0) {
    const observer = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add("show-3d");
            }
        });
    }, {
        threshold: 0.18
    });

    scroll3dElements.forEach(function (element) {
        observer.observe(element);
    });
}

/* Stats counter animation */
const counters = document.querySelectorAll(".counter");

if (counters.length > 0) {
    const counterObserver = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
            if (entry.isIntersecting) {
                const counter = entry.target;
                const target = Number(counter.getAttribute("data-target"));
                let current = 0;
                const speed = 80;
                const increment = Math.ceil(target / speed);

                const updateCounter = function () {
                    current += increment;

                    if (current >= target) {
                        counter.textContent = target.toLocaleString();
                    } else {
                        counter.textContent = current.toLocaleString();
                        requestAnimationFrame(updateCounter);
                    }
                };

                updateCounter();
                counterObserver.unobserve(counter);
            }
        });
    }, {
        threshold: 0.4
    });

    counters.forEach(function (counter) {
        counterObserver.observe(counter);
    });
}
/* Holiday cards horizontal scroll */
function scrollHolidayCards(direction) {
    const slider = document.getElementById("holidaySlider");

    if (!slider) {
        return;
    }

    const scrollAmount = 360;
    slider.scrollBy({
        left: direction * scrollAmount,
        behavior: "smooth"
    });
}

/* Holiday category filter */
function filterHolidayCards(category) {
    const cards = document.querySelectorAll(".holiday-card");
    const tabs = document.querySelectorAll(".holiday-tab");

    tabs.forEach(function (tab) {
        tab.classList.remove("active");
    });

    event.target.classList.add("active");

    cards.forEach(function (card) {
        const cardCategory = card.getAttribute("data-category");

        if (category === "all" || cardCategory === category) {
            card.classList.remove("hide-card");
        } else {
            card.classList.add("hide-card");
        }
    });
}

/* Review form frontend handling */
const reviewForm = document.getElementById("reviewForm");

if (reviewForm) {
    reviewForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = document.getElementById("reviewName").value.trim();
        const email = document.getElementById("reviewEmail").value.trim();
        const eventName = document.getElementById("reviewEvent").value;
        const rating = document.getElementById("reviewRating").value;
        const message = document.getElementById("reviewMessage").value.trim();
        const reviewsList = document.getElementById("reviewsList");

        if (name === "" || email === "" || eventName === "" || rating === "" || message === "") {
            alert("Please fill all review details.");
            return;
        }

        const stars = "★★★★★".slice(0, Number(rating)) + "☆☆☆☆☆".slice(0, 5 - Number(rating));

        const reviewCard = document.createElement("div");
        reviewCard.className = "review-display-card show-3d";

        reviewCard.innerHTML = `
            <div class="review-card-top">
                <div>
                    <h4>${name}</h4>
                    <p>${eventName}</p>
                </div>
                <span class="review-stars">${stars}</span>
            </div>
            <p class="review-text">${message}</p>
        `;

        reviewsList.prepend(reviewCard);

        alert("Review submitted successfully!");
        reviewForm.reset();
    });
}
/* Booking page frontend handling */
const bookingForm = document.getElementById("bookingForm");
const bookingEvent = document.getElementById("bookingEvent");
const ticketQuantity = document.getElementById("ticketQuantity");

const ticketPrice = document.getElementById("ticketPrice");
const totalAmount = document.getElementById("totalAmount");

const summaryName = document.getElementById("summaryName");
const summaryEvent = document.getElementById("summaryEvent");
const summaryDate = document.getElementById("summaryDate");
const summaryTickets = document.getElementById("summaryTickets");
const summaryTotal = document.getElementById("summaryTotal");

function updateBookingTotal() {
    if (!bookingEvent || !ticketQuantity) {
        return;
    }

    const selectedOption = bookingEvent.options[bookingEvent.selectedIndex];
    const price = Number(selectedOption.getAttribute("data-price")) || 0;
    const quantity = Number(ticketQuantity.value) || 1;
    const total = price * quantity;

    if (ticketPrice) {
        ticketPrice.textContent = "Rs. " + price.toLocaleString();
    }

    if (totalAmount) {
        totalAmount.textContent = "Rs. " + total.toLocaleString();
    }

    if (summaryEvent) {
        summaryEvent.textContent = bookingEvent.value || "-";
    }

    if (summaryTickets) {
        summaryTickets.textContent = quantity;
    }

    if (summaryTotal) {
        summaryTotal.textContent = "Rs. " + total.toLocaleString();
    }
}

if (bookingEvent) {
    bookingEvent.addEventListener("change", updateBookingTotal);
}

if (ticketQuantity) {
    ticketQuantity.addEventListener("input", updateBookingTotal);
}

const bookingName = document.getElementById("bookingName");
const bookingDate = document.getElementById("bookingDate");

if (bookingName) {
    bookingName.addEventListener("input", function () {
        summaryName.textContent = bookingName.value.trim() || "-";
    });
}

if (bookingDate) {
    bookingDate.addEventListener("change", function () {
        summaryDate.textContent = bookingDate.value || "-";
    });
}

if (bookingForm) {
    bookingForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = document.getElementById("bookingName").value.trim();
        const email = document.getElementById("bookingEmail").value.trim();
        const phone = document.getElementById("bookingPhone").value.trim();
        const type = document.getElementById("bookingType").value;
        const eventName = document.getElementById("bookingEvent").value;
        const date = document.getElementById("bookingDate").value;
        const quantity = Number(document.getElementById("ticketQuantity").value) || 1;

        const selectedOption = bookingEvent.options[bookingEvent.selectedIndex];
        const price = Number(selectedOption.getAttribute("data-price")) || 0;
        const total = price * quantity;

        if (name === "" || email === "" || phone === "" || type === "" || eventName === "" || date === "") {
            alert("Please fill all required booking details.");
            return;
        }

        const bookingPreviewList = document.getElementById("bookingPreviewList");

        if (bookingPreviewList) {
            const bookingCard = document.createElement("div");
            bookingCard.className = "col-md-4 scroll-3d show-3d";

            bookingCard.innerHTML = `
                <div class="booking-preview-card">
                    <h4>${eventName}</h4>
                    <p>👤 ${name}</p>
                    <p>🎟 ${quantity} Tickets</p>
                    <p>💰 Rs. ${total.toLocaleString()}</p>
                    <span class="booking-status">Pending Payment</span>
                </div>
            `;

            bookingPreviewList.prepend(bookingCard);
        }

        alert("Booking confirmed successfully! Please continue to payment.");
        bookingForm.reset();

        if (ticketPrice) ticketPrice.textContent = "Rs. 0";
        if (totalAmount) totalAmount.textContent = "Rs. 0";
        if (summaryName) summaryName.textContent = "-";
        if (summaryEvent) summaryEvent.textContent = "-";
        if (summaryDate) summaryDate.textContent = "-";
        if (summaryTickets) summaryTickets.textContent = "1";
        if (summaryTotal) summaryTotal.textContent = "Rs. 0";
    });
}
/* Login / Register form switch */
const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");
const loginTab = document.getElementById("loginTab");
const registerTab = document.getElementById("registerTab");

function showLoginForm() {
    if (!loginForm || !registerForm || !loginTab || !registerTab) {
        return;
    }

    loginForm.classList.add("active-form");
    registerForm.classList.remove("active-form");

    loginTab.classList.add("active");
    registerTab.classList.remove("active");
}

function showRegisterForm() {
    if (!loginForm || !registerForm || !loginTab || !registerTab) {
        return;
    }

    registerForm.classList.add("active-form");
    loginForm.classList.remove("active-form");

    registerTab.classList.add("active");
    loginTab.classList.remove("active");
}

/* Login form validation */
if (loginForm) {
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value.trim();

        if (email === "" || password === "") {
            alert("Please enter email and password.");
            return;
        }

        alert("Login successful! Backend connection will be added later.");
        loginForm.reset();
    });
}

/* Register form validation */
if (registerForm) {
    registerForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = document.getElementById("registerName").value.trim();
        const phone = document.getElementById("registerPhone").value.trim();
        const email = document.getElementById("registerEmail").value.trim();
        const password = document.getElementById("registerPassword").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();
        const agreeTerms = document.getElementById("agreeTerms").checked;

        if (name === "" || phone === "" || email === "" || password === "" || confirmPassword === "") {
            alert("Please fill all registration details.");
            return;
        }

        if (password !== confirmPassword) {
            alert("Password and confirm password do not match.");
            return;
        }

        if (!agreeTerms) {
            alert("Please agree to terms and conditions.");
            return;
        }

        alert("Account created successfully! Backend connection will be added later.");
        registerForm.reset();
        showLoginForm();
    });
}
/* Payment page frontend handling */
const paymentForm = document.getElementById("paymentForm");
const paymentBooking = document.getElementById("paymentBooking");
const paymentAmount = document.getElementById("paymentAmount");
const visualAmount = document.getElementById("visualAmount");

const paymentName = document.getElementById("paymentName");
const paymentMethod = document.getElementById("paymentMethod");
const paymentDate = document.getElementById("paymentDate");

const paymentSummaryName = document.getElementById("paymentSummaryName");
const paymentSummaryBooking = document.getElementById("paymentSummaryBooking");
const paymentSummaryMethod = document.getElementById("paymentSummaryMethod");
const paymentSummaryDate = document.getElementById("paymentSummaryDate");
const paymentSummaryTotal = document.getElementById("paymentSummaryTotal");

function updatePaymentSummary() {
    if (!paymentBooking) {
        return;
    }

    const selectedOption = paymentBooking.options[paymentBooking.selectedIndex];
    const amount = Number(selectedOption.getAttribute("data-amount")) || 0;
    const formattedAmount = "Rs. " + amount.toLocaleString();

    if (paymentAmount) {
        paymentAmount.value = formattedAmount;
    }

    if (visualAmount) {
        visualAmount.textContent = formattedAmount;
    }

    if (paymentSummaryBooking) {
        paymentSummaryBooking.textContent = paymentBooking.value || "-";
    }

    if (paymentSummaryTotal) {
        paymentSummaryTotal.textContent = formattedAmount;
    }
}

if (paymentBooking) {
    paymentBooking.addEventListener("change", updatePaymentSummary);
}

if (paymentName) {
    paymentName.addEventListener("input", function () {
        paymentSummaryName.textContent = paymentName.value.trim() || "-";
    });
}

if (paymentMethod) {
    paymentMethod.addEventListener("change", function () {
        paymentSummaryMethod.textContent = paymentMethod.value || "-";
    });
}

if (paymentDate) {
    paymentDate.addEventListener("change", function () {
        paymentSummaryDate.textContent = paymentDate.value || "-";
    });
}

if (paymentForm) {
    paymentForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = document.getElementById("paymentName").value.trim();
        const email = document.getElementById("paymentEmail").value.trim();
        const booking = document.getElementById("paymentBooking").value;
        const method = document.getElementById("paymentMethod").value;
        const date = document.getElementById("paymentDate").value;
        const transactionId = document.getElementById("transactionId").value.trim();
        const referenceName = document.getElementById("referenceName").value.trim();
        const status = document.getElementById("paymentStatus").value;

        const selectedOption = paymentBooking.options[paymentBooking.selectedIndex];
        const amount = Number(selectedOption.getAttribute("data-amount")) || 0;

        if (name === "" || email === "" || booking === "" || method === "" || date === "" || transactionId === "" || referenceName === "") {
            alert("Please fill all payment details.");
            return;
        }

        const paymentHistoryList = document.getElementById("paymentHistoryList");

        if (paymentHistoryList) {
            const paymentCard = document.createElement("div");
            paymentCard.className = "col-md-4 scroll-3d show-3d";

            const statusClass = status.toLowerCase();

            paymentCard.innerHTML = `
                <div class="payment-history-card">
                    <h4>${booking}</h4>
                    <p>👤 ${name}</p>
                    <p>💳 ${method}</p>
                    <p>💰 Rs. ${amount.toLocaleString()}</p>
                    <span class="payment-status ${statusClass}">${status}</span>
                </div>
            `;

            paymentHistoryList.prepend(paymentCard);
        }

        alert("Payment submitted successfully!");

        paymentForm.reset();

        if (paymentAmount) paymentAmount.value = "Rs. 0";
        if (visualAmount) visualAmount.textContent = "Rs. 0";
        if (paymentSummaryName) paymentSummaryName.textContent = "-";
        if (paymentSummaryBooking) paymentSummaryBooking.textContent = "-";
        if (paymentSummaryMethod) paymentSummaryMethod.textContent = "-";
        if (paymentSummaryDate) paymentSummaryDate.textContent = "-";
        if (paymentSummaryTotal) paymentSummaryTotal.textContent = "Rs. 0";
    });
}
/* Save user profile after register and show profile page */
const registerFormForProfile = document.getElementById("registerForm");

if (registerFormForProfile) {
    registerFormForProfile.addEventListener("submit", function (event) {
        event.preventDefault();

        const name = document.getElementById("registerName").value.trim();
        const phone = document.getElementById("registerPhone").value.trim();
        const email = document.getElementById("registerEmail").value.trim();
        const password = document.getElementById("registerPassword").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();
        const agreeTerms = document.getElementById("agreeTerms").checked;

        if (name === "" || phone === "" || email === "" || password === "" || confirmPassword === "") {
            alert("Please fill all registration details.");
            return;
        }

        if (password !== confirmPassword) {
            alert("Password and confirm password do not match.");
            return;
        }

        if (!agreeTerms) {
            alert("Please agree to terms and conditions.");
            return;
        }

        const userProfile = {
            name: name,
            phone: phone,
            email: email,
            password: password
        };

        localStorage.setItem("loggedUser", JSON.stringify(userProfile));

        alert("Account created successfully!");
        window.location.href = "profile.html";
    });
}

/* Login and redirect profile */
const loginFormForProfile = document.getElementById("loginForm");

if (loginFormForProfile) {
    loginFormForProfile.addEventListener("submit", function (event) {
        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value.trim();

        if (email === "" || password === "") {
            alert("Please enter email and password.");
            return;
        }

        const savedUser = JSON.parse(localStorage.getItem("loggedUser"));

        if (!savedUser) {
            alert("No account found. Please sign up first.");
            return;
        }

        if (savedUser.email === email && savedUser.password === password) {
            alert("Login successful!");
            window.location.href = "profile.html";
        } else {
            alert("Invalid email or password.");
        }
    });
}

/* Load profile page data */
const profileForm = document.getElementById("profileForm");

function loadProfileData() {
    const savedUser = JSON.parse(localStorage.getItem("loggedUser"));

    if (!savedUser) {
        return;
    }

    const firstLetter = savedUser.name ? savedUser.name.charAt(0).toUpperCase() : "U";

    const profileAvatar = document.getElementById("profileAvatar");
    const profileDisplayName = document.getElementById("profileDisplayName");
    const profileDisplayEmail = document.getElementById("profileDisplayEmail");

    const profileNameText = document.getElementById("profileNameText");
    const profileEmailText = document.getElementById("profileEmailText");
    const profilePhoneText = document.getElementById("profilePhoneText");

    const profileName = document.getElementById("profileName");
    const profileEmail = document.getElementById("profileEmail");
    const profilePhone = document.getElementById("profilePhone");

    if (profileAvatar) profileAvatar.textContent = firstLetter;
    if (profileDisplayName) profileDisplayName.textContent = savedUser.name;
    if (profileDisplayEmail) profileDisplayEmail.textContent = savedUser.email;

    if (profileNameText) profileNameText.textContent = savedUser.name;
    if (profileEmailText) profileEmailText.textContent = savedUser.email;
    if (profilePhoneText) profilePhoneText.textContent = savedUser.phone;

    if (profileName) profileName.value = savedUser.name;
    if (profileEmail) profileEmail.value = savedUser.email;
    if (profilePhone) profilePhone.value = savedUser.phone;
}

if (profileForm) {
    loadProfileData();

    profileForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const savedUser = JSON.parse(localStorage.getItem("loggedUser"));

        if (!savedUser) {
            alert("No profile found. Please sign up first.");
            window.location.href = "users.html";
            return;
        }

        const updatedName = document.getElementById("profileName").value.trim();
        const updatedPhone = document.getElementById("profilePhone").value.trim();
        const updatedEmail = document.getElementById("profileEmail").value.trim();
        const newPassword = document.getElementById("profilePassword").value.trim();
        const confirmPassword = document.getElementById("profileConfirmPassword").value.trim();

        if (updatedName === "" || updatedPhone === "" || updatedEmail === "") {
            alert("Please fill name, phone and email.");
            return;
        }

        if (newPassword !== "" || confirmPassword !== "") {
            if (newPassword !== confirmPassword) {
                alert("New password and confirm password do not match.");
                return;
            }

            savedUser.password = newPassword;
        }

        savedUser.name = updatedName;
        savedUser.phone = updatedPhone;
        savedUser.email = updatedEmail;

        localStorage.setItem("loggedUser", JSON.stringify(savedUser));

        alert("Profile updated successfully!");
        loadProfileData();

        document.getElementById("profilePassword").value = "";
        document.getElementById("profileConfirmPassword").value = "";
    });
}

function logoutUser() {
    alert("Logged out successfully!");
    window.location.href = "users.html";
}
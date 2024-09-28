/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': '#dd2509',
        'secondary': '#e68a1d',
        'tertiary': '#7f75ec',
        'background': '#d8dbe2',
        'dark': '#424848',
        'success': '#22c55e',
        'danger': '#ef4444',
        'muted': '#94a3b8',
      },
      fontFamily: {
        'opensans': ['Open Sans', 'sans-serif'],
        'mulish': ['Mulish', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
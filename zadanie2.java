import java.io.*;
import java.util.Scanner;

public class zadanie2 {
	private String pliczek_wejsciowy = "duzy1.txt";
	private String pliczek_wyjsciowy = "out.txt";
	private char[][] tablica;
	private int n, m;
	private int najdluzsza_rzeka = 0;
	private int najwieksza_wyspa = 0;
	private int powierzchnia = 0;

	zadanie2() {
		Odczyt_pliku();
		long start = System.currentTimeMillis(); // zaczyna mierzyć czas
		Zwiedzanie_mapy();
		long stop = System.currentTimeMillis(); // zatrzymuje pomiar czasu
		System.out.println("Czas wykonania:" + (stop - start) + " milisekund");
		Zapis_pliczku();
	}

	private void Odczyt_pliku() { // liniowo
		try {
			File pliczek_wejsciowy = new File(this.pliczek_wejsciowy);
			Scanner odczyt_z_pliku = new Scanner(pliczek_wejsciowy);
			this.n = odczyt_z_pliku.nextInt();// odczyt n
			this.m = odczyt_z_pliku.nextInt(); // odczyt m
			if (this.n < 3 || this.m > 1000) {
				System.out.println("w pliku są złe dane - zmień plik na dobry");
				System.exit(-1);
			} // jak bledne dane wejsciowe
			tablica = new char[n][m]; // tablica n x m taka jak ma byc
			String linia = new String(); // bedzie zapisywac pobranie z linii dane
			for (int i = 0; i < n; i++) {
				linia = odczyt_z_pliku.next(); // pobieram cała linie
				for (int j = 0; j < m; j++) {
					tablica[i][j] = linia.charAt(j); // i przechodze po niej znak po znaku żeby zapisac je w tablicy
				}
			}
			odczyt_z_pliku.close();
		} catch (FileNotFoundException x) {
			System.out.println("Podano zły pliczek - nie widzę go :(");
			System.exit(-1);
		}
	}

	// x - ląd
	// o - woda stojąca
	// u - rzeka

	private void Zwiedzanie_mapy() { // liniowo jestem tylko raz w danym miejscu
		for (int w = 0; w < this.n; w++) {
			for (int k = 0; k < this.m; k++) {
				if (tablica[w][k] == 'o') // jeżeli jesteśmy na morzu
					Szukanie(w, k); 
			}
		}
	}

	private void Szukanie(int wiersz, int kolumna) { // szukamy
		{// rzeki
			try {
				if (tablica[wiersz + 1][kolumna] == 'u') { // szukam POD którym 'o' zaczyna się rzeka
					Szukanie_rzeki(wiersz + 1, kolumna, 0); // zacznij szukac rzeki
				}
			} catch (ArrayIndexOutOfBoundsException x) {
				;
			}
		}
		{// wyspy
			try {
				if (tablica[wiersz][kolumna + 1] == 'x') { // szukam OBOK którego 'o' jest ląd - jak po PRAWEJ to na
															// 100% to nie ląd a wyspa
					Szukanie_wyspy(wiersz, kolumna + 1); // zacznij szukac wyspy
					if (powierzchnia - 1 > this.najwieksza_wyspa) //sprawdzamy która wyspa jest największa
						this.najwieksza_wyspa = powierzchnia - 1;
					powierzchnia = 0; // zerujemy dla kolejnej wyspy
				}
			} catch (ArrayIndexOutOfBoundsException y) {
				;
			}
		}
	}

	private void Szukanie_wyspy(int wiersz, int kolumna) { // rekurencja
		powierzchnia++;

		/*
		 * if(tablica[wiersz][kolumna] == 'x') { tablica[wiersz][kolumna] = 'W';
		 * Szukanie_wyspy(wiersz,kolumna); }
		 */

		if (wiersz + 1 < this.n && kolumna - 1 >= 0 && tablica[wiersz + 1][kolumna - 1] == 'x') {
			tablica[wiersz + 1][kolumna - 1] = 'W'; // jak odwiedziliśmy dany x to zamieniamy go na W
			Szukanie_wyspy(wiersz + 1, kolumna - 1);
		}
		if (wiersz + 1 < this.n && tablica[wiersz + 1][kolumna] == 'x') {
			tablica[wiersz + 1][kolumna] = 'W';
			Szukanie_wyspy(wiersz + 1, kolumna);
		}
		if (kolumna + 1 < this.m && wiersz + 1 < this.n && tablica[wiersz + 1][kolumna + 1] == 'x') {
			tablica[wiersz + 1][kolumna + 1] = 'W';
			Szukanie_wyspy(wiersz + 1, kolumna + 1);
		}
		if (kolumna + 1 < this.m && tablica[wiersz][kolumna + 1] == 'x') {
			tablica[wiersz][kolumna + 1] = 'W';
			Szukanie_wyspy(wiersz, kolumna + 1);
		}
		if (kolumna - 1 >= 0 && tablica[wiersz][kolumna - 1] == 'x') {
			tablica[wiersz][kolumna - 1] = 'W';
			Szukanie_wyspy(wiersz, kolumna - 1);
		}
		if (wiersz - 1 >= 0 && kolumna - 1 >= 0 && tablica[wiersz - 1][kolumna - 1] == 'x') {
			tablica[wiersz - 1][kolumna - 1] = 'W';
			Szukanie_wyspy(wiersz - 1, kolumna - 1);
		}
		if (wiersz - 1 >= 0 && tablica[wiersz - 1][kolumna] == 'x') {
			tablica[wiersz - 1][kolumna] = 'W';
			Szukanie_wyspy(wiersz - 1, kolumna);
		}
		if (wiersz - 1 >= 0 && kolumna + 1 < this.m && tablica[wiersz - 1][kolumna + 1] == 'x') {
				tablica[wiersz - 1][kolumna + 1] = 'W';
				Szukanie_wyspy(wiersz - 1, kolumna + 1);
			}

	}

	private void Szukanie_rzeki(int wiersz, int kolumna, int dlugosc_rzeki) { // rekurencja
		dlugosc_rzeki++; // zliczam długość rzeki

		if (wiersz + 1 < this.n && tablica[wiersz + 1][kolumna] == 'u') {
			tablica[wiersz + 1][kolumna] = 'R'; // jak byłam w jakimś 'u' to zamieniam literkę na 'R'
			Szukanie_rzeki(wiersz + 1, kolumna, dlugosc_rzeki);
		}

		if (kolumna + 1 < this.m && tablica[wiersz][kolumna + 1] == 'u') {
			tablica[wiersz][kolumna + 1] = 'R';
			Szukanie_rzeki(wiersz, kolumna + 1, dlugosc_rzeki);
		}

		if (wiersz - 1 >= 0 && tablica[wiersz - 1][kolumna] == 'u') {
			tablica[wiersz - 1][kolumna] = 'R';
			Szukanie_rzeki(wiersz - 1, kolumna, dlugosc_rzeki);
		}

		if (kolumna - 1 >= 0 && tablica[wiersz][kolumna - 1] == 'u') {
			tablica[wiersz][kolumna - 1] = 'R';
			Szukanie_rzeki(wiersz, kolumna - 1, dlugosc_rzeki);
		}

		else if (dlugosc_rzeki > this.najdluzsza_rzeka)
			this.najdluzsza_rzeka = dlugosc_rzeki;
	}

	private void Zapis_pliczku() {
		System.out.println(this.najwieksza_wyspa + " " + this.najdluzsza_rzeka);

		try {
			PrintWriter zapis_do_pliku = new PrintWriter(this.pliczek_wyjsciowy);
			zapis_do_pliku.print(this.najwieksza_wyspa);
			zapis_do_pliku.print(" ");
			zapis_do_pliku.print(this.najdluzsza_rzeka);
			zapis_do_pliku.close();
		} catch (IOException x) {
			System.out.println("blad zapisu wyniku- próbuj dalej");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		zadanie2 obiekt = new zadanie2();
	}
}

from subprocess import call

minLength = 1221312
minString = ""

def findSHortest(a, b):
	print("Min: " + minString)
	global minString
	global minLength

	f = open("sample3.in", "w")
	f.write("1\n" + a + " " + b + "\n")
	f.close()
	call(["sh", "test.sh"])
	f2 = open("temp.out", 'r')
	output = [n for n in f2]
	# print("XXX" + output[0].split()[3])
	if (output[0].split()[3] == '0'):
		return False 
	else:
		print(a, b)
		if len(a) > len(b): return False
		if len(a) + len(b) < minLength:
			minLength = len(a) + len(b)
			minString = a + " " + b
		counter = 0
		for index in range(len(a)):
			if not findSHortest(a[:index] + a[index + 1:], b):
				counter +=1
		counter = 0
		for index in range(len(b)):
			if not findSHortest(a, b[:index] + b[index + 1:]):
				counter += 1
	return False

if __name__ == "__main__":
	a = "ACBBBCDCEDBADBBEABBEDAEADEBAEB"
	b = "AEBEEAEEABAEEBCACDBBAEABCEDCABEEDACEEC"
	# a, b = "ACCBAAE", "ABAEBCB"
	findSHortest(a, b)

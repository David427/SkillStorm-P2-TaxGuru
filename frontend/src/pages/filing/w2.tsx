import {
  Form,
  Grid,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { states } from "../../states";
import { Link, useNavigate } from "react-router-dom";

export default function W2() {
  const navigate = useNavigate();

  const handleW2Info = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      ein: e.currentTarget.elements.ein.value,
      // @ts-expect-error untyped form elements but we need the values
      employerName: e.currentTarget.elements.e_name.value,
      // @ts-expect-error untyped form elements but we need the values
      employerStreetAddress: e.currentTarget.elements.e_street_address.value,
      // @ts-expect-error untyped form elements but we need the values
      employerCity: e.currentTarget.elements.e_city.value,
      // @ts-expect-error untyped form elements but we need the values
      employerZipCode: e.currentTarget.elements.e_zipcode.value,
      // @ts-expect-error untyped form elements but we need the values
      employerState: e.currentTarget.elements.e_state.value,
      // @ts-expect-error untyped form elements but we need the values
      wages: e.currentTarget.elements.wages.value,
      // @ts-expect-error untyped form elements but we need the values
      fedWithheld: e.currentTarget.elements.fed_withheld.value,
      // @ts-expect-error untyped form elements but we need the values
      socialWithheld: e.currentTarget.elements.social_withheld.value,
      // @ts-expect-error untyped form elements but we need the values
      medicareWithheld: e.currentTarget.elements.medicare_withheld.value,
    };

    console.log(formData);
    navigate("/filing/self-employment");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label="Personal Information" status="complete" />
          <StepIndicatorStep label="W-2 Information" status="current" />
          <StepIndicatorStep label="Self Employment" />
          <StepIndicatorStep label="Credits & Deductions" />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleW2Info} className="w-full">
          <Fieldset legend="If you don't have any W2 Information to enter, you can skip this section.">
            {/* Employer ID and Name */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-ein" htmlFor="ein" requiredMarker>
                  Employer ID Number (EIN)
                </Label>
                <span id="hint-ein" className="usa-hint">
                  Box b on W-2 Form
                </span>
                <TextInputMask
                  id="ein"
                  name="ein"
                  type="text"
                  aria-labelledby="label-ein"
                  aria-describedby="hint-ein"
                  mask="__-_______"
                  pattern="^\d{2}-\d{7}"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-e_name" htmlFor="e_name" requiredMarker>
                  Employer Name
                </Label>
                <span id="hint-e_name" className="usa-hint">
                  Box c on W-2 Form
                </span>
                <TextInput id="e_name" name="e_name" type="text" required />
              </Grid>
            </Grid>

            {/* Employer Street Address and City */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="e_street_address" requiredMarker>
                  Employer Street Address
                </Label>
                <TextInput
                  id="e_street_address"
                  name="e_street_address"
                  type="text"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label htmlFor="e_city" requiredMarker>
                  City
                </Label>
                <TextInput id="e_city" name="e_city" type="text" required />
              </Grid>
            </Grid>

            {/* Zip Code and State */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="e_state" requiredMarker>
                  State
                </Label>
                <Select id="e_state" name="e_state" required>
                  <option>- Select -</option>
                  {states.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </Select>
              </Grid>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="e_zipcode" requiredMarker>
                  Zip Code
                </Label>
                <TextInputMask
                  id="e_zipcode"
                  name="e_zipcode"
                  type="text"
                  mask="_____"
                  pattern="^\d{5}"
                  required
                />
              </Grid>
            </Grid>

            {/* Wages and Federal Tax withheld */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-wages" htmlFor="wages" requiredMarker>
                  Wages, tips and other compensation
                </Label>
                <span id="hint-wages" className="usa-hint">
                  Box 1 on W-2 Form
                </span>
                <TextInput
                  id="wages"
                  name="wages"
                  type="number"
                  aria-describedby="hint-wages"
                  aria-labelledby="label-wages"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label
                  id="label-fed-withheld"
                  htmlFor="fed_withheld"
                  requiredMarker
                >
                  Federal Income Tax Withheld
                </Label>
                <span id="hint-fedwithheld" className="usa-hint">
                  Box 2 on W-2 Form
                </span>
                <TextInput
                  id="fed_withheld"
                  name="fed_withheld"
                  type="number"
                  aria-describedby="hint-fed-withheld"
                  aria-labelledby="label-fed-withheld"
                  required
                />
              </Grid>
            </Grid>

            {/* Social Security and Medicare Tax Withheld */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label
                  id="label-social-withheld"
                  htmlFor="social_withheld"
                  requiredMarker
                >
                  Social Security Tax Withheld
                </Label>
                <span id="hint-social-withheld" className="usa-hint">
                  Box 4 on W-2 Form
                </span>
                <TextInput
                  id="social_withheld"
                  name="social_withheld"
                  type="number"
                  aria-describedby="hint-social-withheld"
                  aria-labelledby="label-social-withheld"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label
                  id="label-medicare-withheld"
                  htmlFor="medicare_withheld"
                  requiredMarker
                >
                  Medicare Tax Withheld
                </Label>
                <span id="hint-medicare-withheld" className="usa-hint">
                  Box 2 on W-2 Form
                </span>
                <TextInput
                  id="medicare_withheld"
                  name="medicare_withheld"
                  type="number"
                  aria-describedby="hint-medicare-withheld"
                  aria-labelledby="label-medicare-withheld"
                  required
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/personal"
                  className="usa-button usa-button--outline"
                >
                  Back
                </Link>
                <Link
                  to="/filing/self-employment"
                  className="usa-button usa-button--base"
                >
                  Skip
                </Link>
                <Button type="submit">Continue</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
